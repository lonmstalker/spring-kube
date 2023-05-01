package io.lonmstalker.springkube.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.properties.jwt")
data class JwtProperties(
    var location: String,
    var alias: String,
    var keyPass: String,
    var keyStorePass: String
)