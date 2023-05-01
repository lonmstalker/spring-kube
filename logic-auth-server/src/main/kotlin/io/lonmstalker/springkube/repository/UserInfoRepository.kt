package io.lonmstalker.springkube.repository

import io.lonmstalker.springkube.model.RegUser
import io.lonmstalker.springkube.model.User
import java.util.UUID

interface UserInfoRepository {
    fun findByEmail(email: String): User?
    fun findByUsername(username: String): User?
    fun existsEmail(email: String): Boolean
    fun existsUsername(username: String): Boolean
    fun update(regUser: User): User
    fun insert(regUser: RegUser): RegUser
}