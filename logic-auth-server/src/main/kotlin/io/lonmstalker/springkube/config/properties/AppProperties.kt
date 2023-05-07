package io.lonmstalker.springkube.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.properties")
data class AppProperties(
    val jwt: JwtProperties,
    val auth: AuthProperties,
    val token: TokenProperties
) {

    data class AuthProperties(
        val issuer: String,
        val inviteLink: String,
        val blockedTime: Long
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
}
