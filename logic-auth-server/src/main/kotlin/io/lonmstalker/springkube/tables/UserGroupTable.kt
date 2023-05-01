package io.lonmstalker.springkube.tables

import io.lonmstalker.springkube.model.UserGroup
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime

object UserGroupTable : UUIDTable("user_group") {
    val title = varchar("title", 255)
    val inviteLink = varchar("invite_link", 255).nullable()
    val createdDate = datetime("created_date") // не умеет в таймзоны
    val createdBy = uuid("created_by")

    @JvmStatic
    fun ResultRow.toGroup() = UserGroup(
        id = this[id].value,
        title = this[title],
        inviteLink = this[inviteLink],
        createdDate = this[createdDate],
        createdBy = this[createdBy]
    )
}