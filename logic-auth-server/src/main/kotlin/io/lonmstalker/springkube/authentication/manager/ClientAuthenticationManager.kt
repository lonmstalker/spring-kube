package io.lonmstalker.springkube.authentication.manager

import io.lonmstalker.springkube.config.properties.TokenClients
import io.lonmstalker.springkube.constants.AuthConstants
import io.lonmstalker.springkube.constants.ErrorConstants
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException

class ClientAuthenticationManager(private val tokenClients: TokenClients) : AuthenticationManager {

    override fun authenticate(authentication: Authentication): Authentication {
        val client = tokenClients.clients
            .firstOrNull { it.clientId == authentication.principal }
            ?: throw UsernameNotFoundException(ErrorConstants.USERNAME_NOT_EXISTS)
        if (client.clientSecret == authentication.credentials) {
            return UsernamePasswordAuthenticationToken.authenticated(
                authentication.principal,
                authentication.credentials,
                mutableSetOf(SimpleGrantedAuthority(AuthConstants.ACCESS_ROLE))
            )
        }
        throw BadCredentialsException(ErrorConstants.USER_BAD_CREDENTIALS)
    }
}