package io.lonmstalker.springkube.model

import io.lonmstalker.springkube.enums.UserRole
import io.lonmstalker.springkube.enums.UserStatus
import java.time.LocalDateTime
import java.util.*

data class User(
    val id: UUID,
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String?,
    val invitedBy: UUID?,
    val lastBlocked: LocalDateTime?,
    var lastLogin: LocalDateTime?,
    val createdDate: LocalDateTime,
    var userGroupId: UUID?,
    var userPasswordId: UUID?,
    val loginAttempts: Short,
    val role: UserRole = UserRole.USER,
    val status: UserStatus = UserStatus.ACTIVATED,
)
