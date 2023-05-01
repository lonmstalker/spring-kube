package io.lonmstalker.springkube.tables

import io.lonmstalker.springkube.enums.PasswordType
import io.lonmstalker.springkube.model.UserPassword
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime

object UserPasswordTable : UUIDTable("user_password") {
    val value = varchar("value", 500)
    val userId = reference("user_id", UserTable)
    val type = varchar("type", 50)
    val createdDate = datetime("created_date") // не умеет в таймзоны

    @JvmStatic
    fun ResultRow.toPassword() = UserPassword(
        id = this[id].value,
        userId = this[userId].value,
        value = this[value],
        type = PasswordType.valueOf(this[type]),
        createdDate = this[createdDate]
    )
}