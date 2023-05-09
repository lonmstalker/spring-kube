package io.lonmstalker.springkube.provider.impl

import io.lonmstalker.springkube.config.properties.AppProperties
import io.lonmstalker.springkube.enums.Provider
import io.lonmstalker.springkube.enums.TokenType
import io.lonmstalker.springkube.model.UserTokenInfo
import io.lonmstalker.springkube.model.system.CreateTokenSettings
import io.lonmstalker.springkube.provider.TokenProvider
import io.lonmstalker.springkube.service.TokenService
import io.lonmstalker.springkube.service.UserInfoService
import io.lonmstalker.springkube.utils.OAuth2Utils.exceptionOauth2BadRequest
import org.springframework.stereotype.Component
import java.util.*

@Component
class RefreshTokenProviderImpl(
    private val tokenService: TokenService,
    private val userInfoService: UserInfoService, appProperties: AppProperties
) : TokenProvider {
    private val tokenProperties = appProperties.token

    override fun supportGrantType(provider: Provider): Boolean = Provider.REFRESH_TOKEN == provider

    override fun authenticate(request: Map<String, String>, client: String): UserTokenInfo? =
        this.authenticate(request)
            .run { tokenService.createToken(this, client, CreateTokenSettings(tokenProperties.createRefresh)) }

    private fun authenticate(request: Map<String, String>) = userInfoService.findById(this.getUserId(request))

    private fun getUserId(request: Map<String, String>): UUID {
        return request[Provider.REFRESH_TOKEN.grantType]
            ?.run { tokenService.findByValueAndType(this, TokenType.REFRESH).userId }
            ?: throw throw exceptionOauth2BadRequest("refresh token not found")
    }
}