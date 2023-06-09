package io.lonmstalker.springkube.model

import java.time.OffsetDateTime
import java.util.UUID

data class BotActionAudit(
    val id: UUID = UUID.randomUUID(),
    val userId: UUID,
    val positionId: UUID,
    val createdDate: OffsetDateTime
)
