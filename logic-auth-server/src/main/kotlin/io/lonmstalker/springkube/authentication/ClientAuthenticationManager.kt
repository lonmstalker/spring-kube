package io.lonmstalker.springkube.authentication

import io.lonmstalker.springkube.config.properties.TokenClients
import io.lonmstalker.springkube.constants.ErrorCodes.INTERNAL_SERVER_ERROR
import io.lonmstalker.springkube.constants.ErrorConstants.USERNAME_NOT_EXISTS
import io.lonmstalker.springkube.constants.ErrorConstants.USER_BAD_CREDENTIALS
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class ClientAuthenticationManager(private val tokenClients: TokenClients) : AuthenticationManager {

    override fun authenticate(authentication: Authentication?): Authentication {
        if (authentication is UsernamePasswordAuthenticationToken) {
            val client = tokenClients.clients
                .firstOrNull { it.clientId == authentication.principal }
                ?: throw UsernameNotFoundException(USERNAME_NOT_EXISTS)
            if (client.clientSecret == authentication.credentials) {
                return authentication
            }
            throw BadCredentialsException(USER_BAD_CREDENTIALS)
        }
        throw InternalAuthenticationServiceException(INTERNAL_SERVER_ERROR)
    }
}