package io.lonmstalker.springkube.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.properties")
data class AuthProperties(
    val loginPage: String,
    val failureUrl: String,
    val inviteLink: String,
    val consentPage: String,
)