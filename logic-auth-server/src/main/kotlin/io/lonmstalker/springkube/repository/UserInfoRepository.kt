package io.lonmstalker.springkube.repository

import io.lonmstalker.springkube.model.RegUser
import io.lonmstalker.springkube.model.User
import java.util.*

interface UserInfoRepository {
    fun findByEmail(email: String): User?
    fun findByUsername(username: String): User?
    fun existsEmail(email: String): Boolean
    fun existsUsername(username: String): Boolean
    fun update(regUser: User): User
    fun insert(regUser: RegUser): RegUser
    fun incrementLoginAttempts(username: String)
    fun updateStatus(id: UUID, status: String)
}