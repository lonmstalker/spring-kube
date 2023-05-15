package io.lonmstalker.springkube.model

import io.lonmstalker.springkube.enums.UserRole
import io.lonmstalker.springkube.enums.UserStatus
import java.util.UUID

data class RegUser(
    val id: UUID = UUID.randomUUID(),
    val email: String,
    val username: String?= null,
    val firstName: String,
    val lastName: String? = null,
    val lastLogin: java.time.LocalDateTime? = null,
    val createdDate: java.time.LocalDateTime? = null,
    val userGroupId: UUID? = null,
    val userPasswordId: UUID? = null,
    val invitedBy: UUID? = null,
    val role: UserRole = UserRole.USER,
    val status: UserStatus = UserStatus.ACTIVATED,
)