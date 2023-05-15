package io.lonmstalker.springkube.repository

import io.lonmstalker.springkube.model.RegUser
import io.lonmstalker.springkube.model.User
import java.time.LocalDateTime
import java.util.*

interface UserInfoRepository {
    fun findByEmail(email: String): User?
    fun findByUsername(username: String): User?
    fun findById(id: UUID): User?
    fun existsEmail(email: String): Boolean
    fun existsUsername(username: String): Boolean
    fun update(regUser: User): User
    fun insert(regUser: RegUser): User
    fun incrementLoginAttempts(username: String)
    fun updateStatus(id: UUID, status: String)
    fun updateLastLogin(id: UUID, loginTime: LocalDateTime): LocalDateTime
    fun lockUser(id: UUID, time: LocalDateTime)
    fun unlockUser(id: UUID)
}