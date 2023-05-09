package io.lonmstalker.springkube.authentication.manager

import io.lonmstalker.springkube.authentication.UserOauth2InfoService
import io.lonmstalker.springkube.authentication.token.CustomTokenResponseClient
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationProvider
import org.springframework.web.reactive.function.client.WebClient

class Oauth2AuthenticationManager(userService: UserOauth2InfoService, webClient: WebClient) : AuthenticationManager {
    private val provider = OAuth2LoginAuthenticationProvider(CustomTokenResponseClient(webClient), userService)

    override fun authenticate(authentication: Authentication): Authentication = provider.authenticate(authentication)
}