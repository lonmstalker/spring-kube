package io.lonmstalker.springkube.tables

import io.lonmstalker.springkube.enums.UserRole
import io.lonmstalker.springkube.enums.UserStatus
import io.lonmstalker.springkube.model.RegUser
import io.lonmstalker.springkube.model.User
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime

object UserTable : UUIDTable("user_info") {
    val username = varchar("username", 255).uniqueIndex()
    val email = varchar("email", 255).uniqueIndex()
    val firstName = varchar("first_name", 255)
    val lastName = varchar("last_name", 255).nullable()
    val status = varchar("status", 50)
    val role = varchar("role", 100)
    val userGroupId = reference("user_group_id", UserGroupTable).nullable()
    val createdDate = datetime("created_date") // не умеет в таймзоны
    val lastLogin = datetime("last_login").nullable() // не умеет в таймзоны
    val invitedBy = uuid("invited_by").nullable()
    val currentPasswordId = reference("current_password_id", UserPasswordTable).nullable()
    val loginAttempts = short("login_attempts")
    val lastBlocked = datetime("last_blocked").nullable()

    @JvmStatic
    fun ResultRow.toUser() = User(
        id = this[id].value,
        email = this[email],
        username = this[username],
        firstName = this[firstName],
        lastName = this[lastName],
        invitedBy = this[invitedBy],
        lastLogin = this[lastLogin],
        createdDate = this[createdDate],
        userGroupId = this[userGroupId]?.value, // пользователь может быть без группы
        userPasswordId = this[currentPasswordId]?.value, // пользователи, созданные серез провадйера, могут не иметь пароль
        role = UserRole.valueOf(this[role]),
        status = UserStatus.valueOf(this[status]),
        loginAttempts = this[loginAttempts],
        lastBlocked = this[lastBlocked]
    )
}