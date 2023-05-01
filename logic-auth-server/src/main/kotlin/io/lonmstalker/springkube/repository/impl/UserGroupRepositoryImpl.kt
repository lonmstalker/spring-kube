package io.lonmstalker.springkube.repository.impl

import io.lonmstalker.springkube.helper.ClockHelper
import io.lonmstalker.springkube.model.UserGroup
import io.lonmstalker.springkube.repository.UserGroupRepository
import io.lonmstalker.springkube.tables.UserGroupTable
import io.lonmstalker.springkube.tables.UserGroupTable.toGroup
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserGroupRepositoryImpl(private val clockHelper: ClockHelper) : UserGroupRepository {

    override fun findByGroupById(id: UUID): UserGroup? =
        UserGroupTable
            .select { UserGroupTable.id eq id }
            .firstOrNull()
            ?.toGroup()

    override fun insert(userGroup: UserGroup): UserGroup =
        UserGroupTable
            .insert {
                it[id] = userGroup.id
                it[createdBy] = userGroup.createdBy
                it[createdDate] = clockHelper.clock()
                it[title] = userGroup.title
                it[inviteLink] = userGroup.inviteLink
            }
            .resultedValues!![0]
            .toGroup()

    override fun update(userGroup: UserGroup): UserGroup =
        UserGroupTable
            .update {
                it[title] = userGroup.title
                it[inviteLink] = userGroup.inviteLink
            }
            .run { userGroup }
}