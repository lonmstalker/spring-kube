package io.lonmstalker.springkube.repository.impl

import io.lonmstalker.springkube.enums.UserStatus
import io.lonmstalker.springkube.helper.ClockHelper
import io.lonmstalker.springkube.model.RegUser
import io.lonmstalker.springkube.model.User
import io.lonmstalker.springkube.repository.UserInfoRepository
import io.lonmstalker.springkube.tables.UserTable
import io.lonmstalker.springkube.tables.UserTable.toUser
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
class UserInfoRepositoryImpl(private val clockHelper: ClockHelper) : UserInfoRepository {

    override fun findByEmail(email: String): User? = this.findBy { UserTable.email eq email }

    override fun findByUsername(username: String): User? = this.findBy { UserTable.username eq username }
    override fun findById(id: UUID): User? = this.findBy { UserTable.id eq id }

    override fun existsEmail(email: String): Boolean = !UserTable.select { UserTable.email eq email }.empty()

    override fun existsUsername(username: String): Boolean =
        !UserTable.select { UserTable.username eq username }.empty()

    override fun update(user: User): User =
        UserTable
            .update {
                it[username] = user.username
                it[email] = user.email
                it[firstName] = user.firstName
                it[lastName] = user.lastName
                it[status] = user.status.name
                it[role] = user.role.name
                it[userGroupId] = user.userGroupId
                it[lastLogin] = clockHelper.clock()
                it[invitedBy] = user.invitedBy
                it[currentPasswordId] = user.userPasswordId
            }
            .run { user }

    override fun insert(regUser: RegUser): User =
        UserTable
            .insert {
                it[id] = regUser.id
                it[username] = regUser.username ?: regUser.email
                it[email] = regUser.email
                it[firstName] = regUser.firstName
                it[lastName] = regUser.lastName
                it[status] = regUser.status.name
                it[role] = regUser.role.name
                it[createdDate] = clockHelper.clock()
                it[invitedBy] = regUser.invitedBy
                it[lastLogin] = regUser.lastLogin
            }
            .resultedValues!![0]
            .toUser()

    override fun incrementLoginAttempts(username: String) {
        TransactionManager
            .current()
            .exec("UPDATE \"user_info\" SET login_attempts = login_attempts + 1 WHERE username = '$username'")
    }

    override fun updateStatus(id: UUID, status: String) {
        TransactionManager
            .current()
            .exec("UPDATE \"user_info\" SET status = '$status' WHERE id = '$id'")
    }

    override fun updateLastLogin(id: UUID, loginTime: LocalDateTime): LocalDateTime =
        TransactionManager
            .current()
            .exec("UPDATE \"user_info\" SET last_login = '$loginTime' WHERE id = '$id'")
            .run { loginTime }

    override fun lockUser(id: UUID, time: LocalDateTime) {
        TransactionManager
            .current()
            .exec("UPDATE \"user_info\" SET status = '${UserStatus.BLOCKED.name}', last_blocked = '$time' WHERE id = '$id'")
    }

    override fun unlockUser(id: UUID) {
        TransactionManager
            .current()
            .exec("UPDATE \"user_info\" SET status = '${UserStatus.ACTIVATED.name}', last_blocked = null, login_attempts = 0 WHERE id = '$id'")
    }

    private fun findBy(where: org.jetbrains.exposed.sql.SqlExpressionBuilder.() -> org.jetbrains.exposed.sql.Op<Boolean>) =
        UserTable
            .select(where)
            .firstOrNull()
            ?.toUser()

}