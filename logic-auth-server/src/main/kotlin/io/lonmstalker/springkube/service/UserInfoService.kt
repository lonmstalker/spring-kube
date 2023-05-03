package io.lonmstalker.springkube.service

import io.lonmstalker.springkube.enums.UserStatus
import io.lonmstalker.springkube.model.RegUser
import io.lonmstalker.springkube.model.User
import java.util.UUID

interface UserInfoService {
    fun save(regUser: RegUser, password: String): User
    fun findByUsername(username: String): User
    fun incrementLoginAttempts(username: String)
    fun updateStatus(id: UUID, status: UserStatus)
}