package io.lonmstalker.springkube.model

import java.time.LocalDateTime
import java.util.*

data class UserGroup(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val inviteLink: String?,
    val createdBy: UUID,
    val createdDate: LocalDateTime? = null,
)