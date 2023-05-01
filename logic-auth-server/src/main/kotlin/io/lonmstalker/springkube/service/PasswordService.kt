package io.lonmstalker.springkube.service

import io.lonmstalker.springkube.model.UserPassword

interface PasswordService {
    fun save(password: UserPassword): UserPassword
}