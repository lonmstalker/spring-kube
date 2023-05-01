package io.lonmstalker.springkube.model

import io.lonmstalker.springkube.enums.UserRole
import io.lonmstalker.springkube.enums.UserStatus
import java.util.UUID

data class User(
    val id: UUID = UUID.randomUUID(),
    val email: String,
    val username: String?,
    val firstName: String,
    val lastName: String?,
    val lastLogin: java.time.LocalDateTime?,
    val createdDate: java.time.LocalDateTime?,
    val userGroupId: UUID?,
    val userPasswordId: UUID?,
    val invitedBy: UUID?,
    val role: UserRole = UserRole.USER,
    val status: UserStatus = UserStatus.ACTIVATED,
)