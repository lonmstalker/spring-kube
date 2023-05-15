package io.lonmstalker.springkube.model

import java.time.LocalDateTime
import java.util.*

data class UserInfo(
    val userId: UUID,
    val role: String,
    val email: String,
    val username: String,
    val firstName: String,
    val userGroupId: UUID,
    val loginTime: LocalDateTime
)