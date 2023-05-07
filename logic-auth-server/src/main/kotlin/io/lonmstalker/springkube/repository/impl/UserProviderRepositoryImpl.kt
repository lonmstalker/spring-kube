package io.lonmstalker.springkube.repository.impl

import io.lonmstalker.springkube.model.UserProvider
import io.lonmstalker.springkube.repository.UserProviderRepository
import io.lonmstalker.springkube.tables.UserProviderTable
import io.lonmstalker.springkube.tables.UserProviderTable.toProvider
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserProviderRepositoryImpl : UserProviderRepository {

    override fun insert(userProvider: UserProvider): UserProvider =
        UserProviderTable
            .insert {
                it[id] = userProvider.id
                it[provider] = userProvider.provider.name
                it[userId] = userProvider.userId
                it[providerUserId] = userProvider.providerUserId
                it[username] = userProvider.username
                it[enabled] = userProvider.enabled
            }
            .resultedValues!![0]
            .toProvider()

    override fun findByProviderAndProviderUserId(providerUserId: String, userProvider: String): UserProvider? =
        UserProviderTable
            .select { (UserProviderTable.providerUserId eq providerUserId) and (UserProviderTable.provider eq userProvider) }
            .firstOrNull()
            ?.toProvider()

    override fun delete(userId: UUID, userProvider: String): Int =
        UserProviderTable
            .deleteWhere { (UserProviderTable.userId eq userId) and (provider eq userProvider) }

}