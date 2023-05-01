package io.lonmstalker.springkube.repository

import io.lonmstalker.springkube.model.UserPassword
import java.util.UUID

interface PasswordRepository {
    fun findLastById(id: UUID): UserPassword?
    fun insert(password: UserPassword): UserPassword
    fun findLastByUser(userId: UUID, lastCount: Int): List<UserPassword>
}