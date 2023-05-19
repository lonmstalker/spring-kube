package io.lonmstalker.springkube.model

import org.telegram.telegrambots.meta.api.methods.ActionType
import java.time.OffsetDateTime
import java.util.UUID

data class BotAction(
    val id: UUID,
    val title: String,
    val type: ActionType,
    val data: Any,
    val locale: String,
    val userGroupId: UUID,
    val createdBy: UUID,
    val createdDate: OffsetDateTime
)
