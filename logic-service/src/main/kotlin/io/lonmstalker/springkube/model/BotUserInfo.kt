package io.lonmstalker.springkube.model

import io.lonmstalker.springkube.enums.UserStatus
import java.time.OffsetDateTime
import java.util.UUID

data class BotUserInfo(
    val id: UUID = UUID.randomUUID(),
    val botId: UUID,
    val telegramId: String,
    val username: String,
    val email: String?,
    val phone: String?,
    val fullName: String?,
    val currentLocale: String,
    val currentPosition: String,
    val status: UserStatus = UserStatus.ACTIVE,
    val createdDate: OffsetDateTime
)
