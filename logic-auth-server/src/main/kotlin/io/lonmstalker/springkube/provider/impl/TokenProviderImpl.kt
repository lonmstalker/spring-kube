package io.lonmstalker.springkube.provider.impl

import io.lonmstalker.springkube.config.properties.AppProperties
import io.lonmstalker.springkube.constants.ErrorConstants.GRANT_TYPE_UNKNOWN
import io.lonmstalker.springkube.enums.Provider
import io.lonmstalker.springkube.exception.AuthException
import io.lonmstalker.springkube.model.CreateTokenSettings
import io.lonmstalker.springkube.model.CustomUserDetails
import io.lonmstalker.springkube.model.UserTokenInfo
import io.lonmstalker.springkube.provider.TokenProvider
import io.lonmstalker.springkube.service.TokenService
import io.lonmstalker.springkube.service.UserInfoService
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.GRANT_TYPE
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Component
class TokenProviderImpl(
    private val tokenService: TokenService,
    private val userInfoService: UserInfoService,
    private val provider: AuthenticationProvider, appProperties: AppProperties
) : TokenProvider {
    private val tokenProperties = appProperties.token

    override fun getUserId(token: String): UUID = this.tokenService.parseToken(token)

    @Transactional
    override fun tryAuthenticate(request: Map<String, String>, client: String): UserTokenInfo? {
        val grantType = request[GRANT_TYPE]
            ?: throw AuthException(GRANT_TYPE_UNKNOWN, "grant_type not found")
        val details = this.authenticate(request, Provider.valueOf(grantType.uppercase())).principal as CustomUserDetails
        return this.tokenService.createToken(details.user, client, CreateTokenSettings(tokenProperties.createRefresh))
    }

    private fun authenticate(request: Map<String, String>, provider: Provider): Authentication =
        try {
            val login = request[provider.login]
                ?: throw AuthException(GRANT_TYPE_UNKNOWN, "login not found")
            val password = request[provider.password]
                ?: throw AuthException(GRANT_TYPE_UNKNOWN, "password not found")
            this.provider.authenticate(unauthenticated(login, password))
        } catch (ex: BadCredentialsException) {
            this.userInfoService.incrementLoginAttempts(request[provider.login]!!)
            throw ex
        }

}