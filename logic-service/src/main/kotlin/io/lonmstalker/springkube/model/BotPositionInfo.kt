package io.lonmstalker.springkube.model

import java.time.OffsetDateTime
import java.util.UUID

data class BotPositionInfo(
    val id: UUID,
    val title: String,
    val botId: UUID,
    val actionId: UUID,
    val fromPosition: String,
    val toPosition: String?,
    val userGroupId: UUID,
    val locale: String,
    val createdBy: UUID,
    val createdDate: OffsetDateTime,
    val version: Int
)
