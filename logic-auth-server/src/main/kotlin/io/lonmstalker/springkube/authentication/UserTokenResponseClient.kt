package io.lonmstalker.springkube.authentication

import io.lonmstalker.springkube.service.UserInfoService
import io.lonmstalker.springkube.service.UserProviderService
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse
import org.springframework.stereotype.Component

@Component
class UserTokenResponseClient(
    private val userInfoService: UserInfoService,
    private val provUserProviderService: UserProviderService,
) : OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    override fun getTokenResponse(authorizationGrantRequest: OAuth2AuthorizationCodeGrantRequest?): OAuth2AccessTokenResponse {
        TODO("Not yet implemented")
    }
}