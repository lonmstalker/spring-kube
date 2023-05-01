package io.lonmstalker.springkube.model

import io.lonmstalker.springkube.enums.PasswordType
import java.time.LocalDateTime
import java.util.*

data class UserPassword(
    val id: UUID = UUID.randomUUID(),
    val userId: UUID,
    val value: String,
    val createdDate: LocalDateTime? = null,
    val type: PasswordType = PasswordType.LONG_LIVE,
)