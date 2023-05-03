package io.lonmstalker.springkube.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.security")
data class TokenClients(
    val clients: List<LogicClient>
)

data class LogicClient(
    val clientId: String,
    val clientSecret: String
)