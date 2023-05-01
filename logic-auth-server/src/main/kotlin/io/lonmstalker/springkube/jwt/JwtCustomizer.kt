package io.lonmstalker.springkube.jwt

import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer

class JwtCustomizer : OAuth2TokenCustomizer<JwtEncodingContext> {
    override fun customize(context: JwtEncodingContext?) {
        TODO("Not yet implemented")
    }
}