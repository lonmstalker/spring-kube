package io.lonmstalker.springkube.model

import io.lonmstalker.springkube.enums.TokenType
import java.time.LocalDateTime
import java.util.*

data class UserToken(
    val id: UUID = UUID.randomUUID(),
    val userId: UUID,
    val value: String,
    val client: String,
    val createdDate: LocalDateTime,
    val type: TokenType = TokenType.ACCESS,
)
