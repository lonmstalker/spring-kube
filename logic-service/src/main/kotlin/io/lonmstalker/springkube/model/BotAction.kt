package io.lonmstalker.springkube.model

import io.lonmstalker.springkube.enums.DefaultActions
import java.time.OffsetDateTime
import java.util.*

data class BotAction(
    val id: UUID? = UUID.randomUUID(),
    val title: String,
    val type: DefaultActions,
    val data: Any,
    val locale: String,
    val userGroupId: UUID?,
    val createdBy: UUID?,
    val createdDate: OffsetDateTime?,
    val version: Int = 0
)
