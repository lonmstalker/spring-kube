package io.lonmstalker.springkube.authentication.token

import io.lonmstalker.springkube.constants.AuthConstants.CLIENT_ID
import io.lonmstalker.springkube.constants.AuthConstants.CLIENT_SECRET
import io.lonmstalker.springkube.constants.AuthConstants.CODE
import io.lonmstalker.springkube.constants.AuthConstants.REAL_TOKEN
import io.lonmstalker.springkube.constants.AuthConstants.REDIRECT_URI
import io.lonmstalker.springkube.constants.AuthConstants.VK_PROVIDER
import io.lonmstalker.springkube.model.oauth2.VkTokenResponse
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse
import org.springframework.web.reactive.function.client.WebClient

class CustomTokenResponseClient(private val webClient: WebClient) :
    OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
    private val defaultResponseClient = DefaultAuthorizationCodeTokenResponseClient()

    override fun getTokenResponse(request: OAuth2AuthorizationCodeGrantRequest): OAuth2AccessTokenResponse? =
        when (request.clientRegistration.clientName) {
            VK_PROVIDER -> this.vkToken(request)
            else -> this.defaultResponseClient.getTokenResponse(request)
        }

    private fun vkToken(request: OAuth2AuthorizationCodeGrantRequest): OAuth2AccessTokenResponse? {
        val reg = request.clientRegistration
        val exchange = request.authorizationExchange.authorizationResponse
        return this.webClient
            .get()
            .uri(reg.providerDetails.tokenUri) {
                it.queryParam(CLIENT_ID, reg.clientId).queryParam(CLIENT_SECRET, reg.clientSecret)
                    .queryParam(CODE, exchange.code).queryParam(REDIRECT_URI, exchange.redirectUri)
                    .build()
            }
            .retrieve()
            .bodyToMono(VkTokenResponse::class.java)
            .map { this.toOAuth2AccessTokenResponse(it) }
            .block()
    }

    private fun toOAuth2AccessTokenResponse(body: VkTokenResponse) =
        OAuth2AccessTokenResponse.withToken(body.accessToken)
            .tokenType(OAuth2AccessToken.TokenType.BEARER)
            .expiresIn(body.expiresIn)
            .additionalParameters(mapOf(REAL_TOKEN to body))
            .build()

}