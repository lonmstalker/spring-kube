package io.lonmstalker.springkube.model

import io.lonmstalker.springkube.enums.BotStatus
import java.time.OffsetDateTime
import java.util.*

data class Bot(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val username: String,
    val url: String,
    val hash: String,
    val createdBy: UUID?,
    val userGroupId: UUID?,
    val createdDate: OffsetDateTime?,
    val version: Int = 0,
    val status: BotStatus = BotStatus.ENABLED,
)
