package io.lonmstalker.springkube.authentication.manager

import io.lonmstalker.springkube.authentication.UserOauth2InfoService
import io.lonmstalker.springkube.config.properties.TokenClients
import io.lonmstalker.springkube.constants.ErrorCodes.INTERNAL_SERVER_ERROR
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class DelegateAuthenticationManager(
    webClient: WebClient,
    tokenClients: TokenClients,
    userService: UserOauth2InfoService
) : AuthenticationManager {

    private val clientManager = ClientAuthenticationManager(tokenClients)
    private val oauth2Manager = Oauth2AuthenticationManager(userService, webClient)

    override fun authenticate(authentication: Authentication): Authentication =
        when(authentication) {
            is UsernamePasswordAuthenticationToken -> this.clientManager.authenticate(authentication)
            is OAuth2LoginAuthenticationToken -> this.oauth2Manager.authenticate(authentication)
            else -> throw InternalAuthenticationServiceException(INTERNAL_SERVER_ERROR)
        }

}