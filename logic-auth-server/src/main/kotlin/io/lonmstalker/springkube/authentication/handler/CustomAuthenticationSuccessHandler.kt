package io.lonmstalker.springkube.authentication.handler

import io.lonmstalker.springkube.config.properties.AppProperties
import io.lonmstalker.springkube.model.UserTokenInfo
import io.lonmstalker.springkube.model.oauth2.CustomOAuth2User
import io.lonmstalker.springkube.model.system.CreateTokenSettings
import io.lonmstalker.springkube.service.TokenService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

class CustomAuthenticationSuccessHandler(
    private val properties: AppProperties,
    private val tokenService: TokenService,
) : AbstractAuthenticationTargetUrlRequestHandler(), AuthenticationSuccessHandler {

    init {
        super.setDefaultTargetUrl(properties.auth.redirectUrl)
    }

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        if (authentication is OAuth2AuthenticationToken) {
            this.setCookie(response, this.createToken(authentication))
        }
        super.handle(request, response, authentication)
    }

    private fun setCookie(response: HttpServletResponse, tokenInfo: UserTokenInfo) {
        response.addCookie(
            Cookie(properties.cookie.accessJwt, tokenInfo.accessToken)
                .apply {
                    this.maxAge = properties.cookie.accessMaxAge
                    this.setDomainAndSecure()
                }
        )
        tokenInfo.refreshToken?.let {
            response.addCookie(
                Cookie(properties.cookie.refreshJwt, it)
                    .apply {
                        this.maxAge = properties.cookie.refreshMaxAge
                        this.setDomainAndSecure()
                    }
            )
        }
    }

    private fun Cookie.setDomainAndSecure() {
        this.secure = true
        this.isHttpOnly = true
        this.domain = properties.cookie.domain
    }

    private fun createToken(authentication: OAuth2AuthenticationToken): UserTokenInfo {
        val principal = authentication.principal as CustomOAuth2User
        return this.tokenService.createToken(
            principal.user,
            authentication.authorizedClientRegistrationId,
            CreateTokenSettings()
        )
    }
}