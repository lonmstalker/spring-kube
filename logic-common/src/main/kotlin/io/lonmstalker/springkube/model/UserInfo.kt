package io.lonmstalker.springkube.model

import java.time.OffsetDateTime
import java.util.UUID

data class UserInfo(
    val userId: UUID,
    val username: String,
    val userGroupId: UUID,
    val authorities: Set<String>,
    val loginTime: OffsetDateTime
)