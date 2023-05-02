package io.lonmstalker.springkube.model

import java.time.OffsetDateTime
import java.util.UUID

data class UserInfo(
    val userId: UUID,
    val role: String,
    val email: String,
    val username: String,
    val firstName: String,
    val userGroupId: UUID,
    val loginTime: OffsetDateTime
)