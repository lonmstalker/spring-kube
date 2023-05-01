package io.lonmstalker.springkube.service

import io.lonmstalker.springkube.model.UserPassword
import java.util.UUID

interface PasswordService {
    fun findById(id: UUID): UserPassword
    fun save(password: UserPassword): UserPassword
}