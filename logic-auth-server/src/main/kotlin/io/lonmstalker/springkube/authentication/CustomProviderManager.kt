package io.lonmstalker.springkube.authentication

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class CustomProviderManager(private val provider: AuthenticationProvider) : AuthenticationManager {

    override fun authenticate(authentication: Authentication): Authentication =
        provider
            .authenticate(
                UsernamePasswordAuthenticationToken(
                    authentication.principal,
                    authentication.credentials,
                    authentication.authorities
                )
            )
}