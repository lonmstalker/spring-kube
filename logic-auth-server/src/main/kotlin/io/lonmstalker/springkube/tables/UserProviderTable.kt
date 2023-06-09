package io.lonmstalker.springkube.tables

import io.lonmstalker.springkube.enums.OidcProvider
import io.lonmstalker.springkube.model.User
import io.lonmstalker.springkube.model.UserProvider
import io.lonmstalker.springkube.tables.UserTable.toUser
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow

object UserProviderTable : UUIDTable("user_provider_info") {
    val provider = varchar("provider", 100)
    val userId = reference("user_id", UserTable)
    val providerUserId = varchar("provider_user_id", 255)
    val username = varchar("username", 255)
    val enabled = bool("enabled")

    @JvmStatic
    fun ResultRow.toProvider(user: User? = null) = UserProvider(
        id = this[id].value,
        userId = this[userId].value,
        provider = OidcProvider.valueOf(this[provider]),
        providerUserId = this[providerUserId],
        username = this[username],
        enabled = this[enabled],
        user = user
    )

    @JvmStatic
    fun ResultRow.toProviderWithUser() = this.toProvider(this.toUser())
}