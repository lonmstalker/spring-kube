package io.lonmstalker.springkube.service

import io.lonmstalker.springkube.model.RegUser
import io.lonmstalker.springkube.model.User

interface UserInfoService {
    fun save(regUser: RegUser, password: String): User
    fun findByUsername(username: String): User
}