package io.lonmstalker.springkube.repository

import io.lonmstalker.springkube.model.UserGroup
import java.util.UUID

interface UserGroupRepository {
    fun findByGroupById(id: UUID): UserGroup?
    fun insert(userGroup: UserGroup): UserGroup
    fun update(userGroup: UserGroup): UserGroup
}