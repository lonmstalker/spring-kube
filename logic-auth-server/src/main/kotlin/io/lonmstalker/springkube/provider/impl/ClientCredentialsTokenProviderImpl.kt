package io.lonmstalker.springkube.provider.impl

import io.lonmstalker.springkube.config.properties.AppProperties
import io.lonmstalker.springkube.enums.Provider
import io.lonmstalker.springkube.model.CreateTokenSettings
import io.lonmstalker.springkube.model.CustomUserDetails
import io.lonmstalker.springkube.model.UserTokenInfo
import io.lonmstalker.springkube.provider.TokenProvider
import io.lonmstalker.springkube.service.TokenService
import io.lonmstalker.springkube.utils.OAuth2Utils.toUsernamePasswordToken
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ClientCredentialsTokenProviderImpl(
    private val tokenService: TokenService,
    private val provider: AuthenticationProvider, appProperties: AppProperties
) : TokenProvider {
    private val tokenProperties = appProperties.token

    override fun supportGrantType(provider: Provider): Boolean = Provider.CLIENT_CREDENTIALS == provider

    @Transactional
    override fun authenticate(request: Map<String, String>, client: String): UserTokenInfo? =
        this.authenticate(request)
            .run { tokenService.createToken(this.user, client, CreateTokenSettings(tokenProperties.createRefresh)) }

    private fun authenticate(request: Map<String, String>): CustomUserDetails =
        this.provider
            .authenticate(request.toUsernamePasswordToken())
            .principal as CustomUserDetails

}