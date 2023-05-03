package io.lonmstalker.springkube.tables

import io.lonmstalker.springkube.enums.TokenType
import io.lonmstalker.springkube.model.UserToken
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.timestamp

object TokenTable : UUIDTable("user_token") {
    val value = varchar("value", 1000)
    val userId = reference("user_id", UserTable)
    val type = varchar("type", 50)
    val client = varchar("client", 50)
    val createdDate = timestamp("created_date") // не умеет в таймзоны

    @JvmStatic
    fun ResultRow.toToken() = UserToken(
        id = this[id].value,
        userId = this[userId].value,
        value = this[value],
        type = TokenType.valueOf(this[type]),
        createdDate = this[createdDate],
        client = this[client]
    )
}