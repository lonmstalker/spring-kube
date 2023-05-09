package io.lonmstalker.springkube.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.properties")
data class AppProperties(
    val cookie: Cookie,
    val jwt: JwtProperties,
    val auth: AuthProperties,
    val token: TokenProperties
) {

    data class AuthProperties(
        val issuer: String,
        val inviteLink: String,
        val blockedTime: Long,
        val redirectUrl: String
    )

    data class JwtProperties(
        var location: String,
        var alias: String,
        var keyPass: String,
        var keyStorePass: String
    )

    data class TokenProperties(
        val accessTtl: Long,
        val refreshTtl: Long,
        val createRefresh: Boolean
    )

    data class Cookie(
        val accessJwt: String,
        val refreshJwt: String,
        val accessMaxAge: Int,
        val refreshMaxAge: Int,
        val domain: String
    )
}
