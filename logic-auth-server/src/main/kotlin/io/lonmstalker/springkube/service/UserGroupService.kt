package io.lonmstalker.springkube.service

import io.lonmstalker.springkube.model.UserGroup
import java.util.*

interface UserGroupService {
    fun findById(id: UUID): UserGroup
    fun saveBy(username: String, userId: UUID): UserGroup
    fun update(userGroup: UserGroup): UserGroup
}