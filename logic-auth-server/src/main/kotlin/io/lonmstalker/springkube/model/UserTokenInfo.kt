package io.lonmstalker.springkube.model

import java.time.Instant

data class UserTokenInfo(
    val tokenType: String,
    val expiresIn: Instant,
    val scope: Set<String>?,
    val accessToken: String,
    val refreshToken: String?,
)