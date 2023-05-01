package io.lonmstalker.springkube.repository.impl

import io.lonmstalker.springkube.helper.ClockHelper
import io.lonmstalker.springkube.model.User
import io.lonmstalker.springkube.repository.UserInfoRepository
import io.lonmstalker.springkube.tables.UserTable
import io.lonmstalker.springkube.tables.UserTable.toUser
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserInfoRepositoryImpl(private val clockHelper: ClockHelper) : UserInfoRepository {

    override fun findByEmail(email: String): User? = this.findBy { UserTable.email eq email }

    override fun findByUsername(username: String): User? = this.findBy { UserTable.username eq username }

    override fun existsEmail(email: String): Boolean = !UserTable.select { UserTable.email eq email }.empty()

    override fun existsUsername(username: String): Boolean = !UserTable.select { UserTable.username eq username }.empty()

    override fun update(user: User, groupId: UUID): User =
        UserTable
            .update {
                it[username] = user.username
                it[email] = user.email
                it[firstName] = user.firstName
                it[lastName] = user.lastName
                it[status] = user.status.name
                it[role] = user.role.name
                it[userGroupId] = groupId
                it[lastLogin] = clockHelper.clock()
                it[invitedBy] = user.invitedBy
                it[currentPasswordId] = user.userPasswordId
            }
            .run { user }

    override fun insert(user: User): User =
        UserTable
            .insert {
                it[id] = user.id
                it[username] = user.username
                it[email] = user.email
                it[firstName] = user.firstName
                it[lastName] = user.lastName
                it[status] = user.status.name
                it[role] = user.role.name
                it[createdDate] = clockHelper.clock()
                it[invitedBy] = user.invitedBy
            }
            .resultedValues!![0]
            .toUser()

    private fun findBy(where: org.jetbrains.exposed.sql.SqlExpressionBuilder.() -> org.jetbrains.exposed.sql.Op<Boolean>) =
        UserTable
            .select(where)
            .firstOrNull()
            ?.toUser()
}