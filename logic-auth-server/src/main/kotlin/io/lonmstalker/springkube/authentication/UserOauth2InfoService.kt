package io.lonmstalker.springkube.authentication

import io.lonmstalker.springkube.constants.AuthConstants.ACCESS_TOKEN
import io.lonmstalker.springkube.constants.AuthConstants.REAL_TOKEN
import io.lonmstalker.springkube.constants.AuthConstants.VK_CURRENT_VERSION
import io.lonmstalker.springkube.constants.AuthConstants.VK_VERSION
import io.lonmstalker.springkube.constants.ErrorConstants.OAUTH2_PROVIDER_DISABLED
import io.lonmstalker.springkube.constants.ErrorConstants.OAUTH2_PROVIDER_UNKNOWN
import io.lonmstalker.springkube.enums.OidcProvider
import io.lonmstalker.springkube.exception.AuthException
import io.lonmstalker.springkube.model.RegUser
import io.lonmstalker.springkube.model.User
import io.lonmstalker.springkube.model.UserProvider
import io.lonmstalker.springkube.model.oauth2.CustomOAuth2User
import io.lonmstalker.springkube.model.oauth2.VkTokenResponse
import io.lonmstalker.springkube.model.oauth2.VkUserInfo
import io.lonmstalker.springkube.service.UserInfoService
import io.lonmstalker.springkube.service.UserProviderService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient

@Component
class UserOauth2InfoService(
    private val webClient: WebClient,
    private val userInfoService: UserInfoService,
    private val userProviderService: UserProviderService
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Transactional
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val provider = this.getProvider(userRequest)
        val userProvider = this.getUserProvider(userRequest, provider)
        return userProvider
            ?.let { this.checkUserEnabled(it) }
            ?: this.regUser(userRequest, provider)
    }

    fun regUser(regUser: RegUser, userProvider: UserProvider): User {
        val user = this.userInfoService.saveWithoutPassword(regUser)
        this.userProviderService.insert(userProvider)
        return user.first
    }

    private fun regUser(userRequest: OAuth2UserRequest, provider: OidcProvider): OAuth2User =
        when (provider) {
            OidcProvider.GOOGLE -> TODO()
            OidcProvider.VK -> this.vkRegUser(userRequest)
        }

    private fun checkUserEnabled(provider: UserProvider) =
        if (provider.enabled) {
            CustomOAuth2User(provider.user!!)
        } else {
            throw AuthException(OAUTH2_PROVIDER_DISABLED, "provider for user disabled")
        }

    private fun getUserProvider(userRequest: OAuth2UserRequest, provider: OidcProvider) =
        when (provider) {
            OidcProvider.VK -> this.vkLoadUser(userRequest)
            else -> throw AuthException(OAUTH2_PROVIDER_UNKNOWN, "provider ${provider.name} unknown")
        }

    private fun vkRegUser(userRequest: OAuth2UserRequest): OAuth2User {
        val vkToken = userRequest.additionalParameters[REAL_TOKEN] as VkTokenResponse
        val userInfo = this.vkUserInfo(userRequest, vkToken)
        return CustomOAuth2User(this.regUser(userInfo, this.createVkUserProvider(userInfo, vkToken)))
    }

    private fun vkLoadUser(userRequest: OAuth2UserRequest): UserProvider? {
        val vkToken = userRequest.additionalParameters[REAL_TOKEN] as VkTokenResponse
        return this.userProviderService.findByProviderAndProviderUserId(vkToken.userId, OidcProvider.VK)
    }

    private fun vkUserInfo(userRequest: OAuth2UserRequest, vkToken: VkTokenResponse) =
        this.webClient
            .get()
            .uri(userRequest.clientRegistration.providerDetails.userInfoEndpoint.uri) {
                it.queryParam(VK_USER_IDS, vkToken.userId)
                    .queryParam(ACCESS_TOKEN, vkToken.accessToken)
                    .queryParam(VK_FIELDS, VK_NEEDED_FIELDS)
                    .queryParam(VK_VERSION, VK_CURRENT_VERSION)
                    .build()
            }
            .retrieve()
            .bodyToMono(VkUserInfo::class.java)
            .block()!!
            .let {
                val user = it.response.first()
                RegUser(
                    email = vkToken.email,
                    firstName = user.firstName,
                    lastName = user.lastName
                )
            }

    private fun createVkUserProvider(user: RegUser, tokenResponse: VkTokenResponse) =
        UserProvider(
            provider = OidcProvider.VK,
            providerUserId = tokenResponse.userId,
            userId = user.id,
            username = tokenResponse.email
        )

    private fun getProvider(userRequest: OAuth2UserRequest) =
        userRequest
            .clientRegistration
            .clientName
            .uppercase()
            .run { OidcProvider.valueOf(this) }

    companion object {
        private const val VK_USER_IDS = "user_ids"
        private const val VK_FIELDS = "fields"
        private val VK_NEEDED_FIELDS = listOf("first_name", "last_name")
    }
}