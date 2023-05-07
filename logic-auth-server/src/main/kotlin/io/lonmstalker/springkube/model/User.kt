package io.lonmstalker.springkube.model

import io.lonmstalker.springkube.enums.UserRole
import io.lonmstalker.springkube.enums.UserStatus
import java.util.*

data class User(
    val id: UUID,
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String?,
    val invitedBy: UUID?,
    val lastLogin: java.time.LocalDateTime?,
    val createdDate: java.time.LocalDateTime,
    var userGroupId: UUID,
    val userPasswordId: UUID,
    val loginAttempts: Short,
    val role: UserRole = UserRole.USER,
    val status: UserStatus = UserStatus.ACTIVATED,
)
