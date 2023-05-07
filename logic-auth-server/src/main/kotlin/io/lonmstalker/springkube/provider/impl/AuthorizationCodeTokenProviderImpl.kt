package io.lonmstalker.springkube.provider.impl

import io.lonmstalker.springkube.config.properties.AppProperties
import io.lonmstalker.springkube.enums.Provider
import io.lonmstalker.springkube.model.UserTokenInfo
import io.lonmstalker.springkube.provider.TokenProvider
import io.lonmstalker.springkube.service.TokenService
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.stereotype.Component

@Component
class AuthorizationCodeTokenProviderImpl(
    private val tokenService: TokenService,
    private val provider: AuthenticationProvider, appProperties: AppProperties
) : TokenProvider {

    override fun supportGrantType(provider: Provider): Boolean = Provider.AUTHORIZATION_CODE == provider

    override fun authenticate(request: Map<String, String>, client: String): UserTokenInfo? {
        TODO("Not yet implemented")
    }
}