package io.lonmstalker.springkube.service

import io.lonmstalker.springkube.enums.UserStatus
import io.lonmstalker.springkube.model.RegUser
import io.lonmstalker.springkube.model.User
import io.lonmstalker.springkube.model.UserGroup
import java.time.LocalDateTime
import java.util.UUID

interface UserInfoService {
    fun save(regUser: RegUser, password: String): User
    fun saveWithoutPassword(regUser: RegUser, updateWithGroup: Boolean): Pair<User, UserGroup>
    fun findByUsername(username: String): User
    fun findById(id: UUID): User
    fun incrementLoginAttempts(username: String)
    fun updateStatus(id: UUID, status: UserStatus)
    fun updateLastLogin(id: UUID, loginTime: LocalDateTime): LocalDateTime
    fun lockUser(id: UUID, time: LocalDateTime)
    fun unlockUser(id: UUID)
}