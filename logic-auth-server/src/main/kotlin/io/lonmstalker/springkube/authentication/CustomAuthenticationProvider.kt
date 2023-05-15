package io.lonmstalker.springkube.authentication

import io.lonmstalker.springkube.service.UserInfoService
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication

open class CustomAuthenticationProvider(
    private val userInfoService: UserInfoService,
    private val delegateAuthenticationProvider: AuthenticationProvider
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication =
        try {
            this.delegateAuthenticationProvider.authenticate(authentication)
        } catch (ex: BadCredentialsException) {
            this.userInfoService.incrementLoginAttempts(authentication.name)
            throw ex
        }

    override fun supports(authentication: Class<*>?): Boolean =
        this.delegateAuthenticationProvider.supports(authentication)
}