package io.lonmstalker.springkube.service

import io.lonmstalker.springkube.model.User

interface UserInfoService {
    fun save(user: User, password: String): User
}