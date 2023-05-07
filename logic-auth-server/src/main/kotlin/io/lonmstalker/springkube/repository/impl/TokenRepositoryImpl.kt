package io.lonmstalker.springkube.repository.impl

import io.lonmstalker.springkube.enums.TokenType
import io.lonmstalker.springkube.model.UserToken
import io.lonmstalker.springkube.repository.TokenRepository
import io.lonmstalker.springkube.tables.TokenTable
import io.lonmstalker.springkube.tables.TokenTable.toToken
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class TokenRepositoryImpl : TokenRepository {

    override fun save(userToken: UserToken): UserToken =
        TokenTable
            .insert { insert(it, userToken) }
            .resultedValues!![0]
            .toToken()

    override fun saveAll(userTokens: Collection<UserToken>) {
        TokenTable
            .batchInsert(userTokens) { insert(this, it) }
    }

    override fun findByValueAndType(value: String, type: String): UserToken? =
        TokenTable
            .select { (TokenTable.type eq type) and (TokenTable.value eq value) }
            .firstOrNull()
            ?.toToken()

    override fun findByTypeAndUserId(userId: UUID, type: TokenType): UserToken? =
        TokenTable
            .select { (TokenTable.userId eq userId) and (TokenTable.type eq type.name) }
            .firstOrNull()
            ?.toToken()

    override fun deleteByClientAndUserId(userId: UUID, client: String): Int =
        TokenTable
            .deleteWhere { (TokenTable.userId eq userId) and (TokenTable.client eq client) }

    private fun insert(body: InsertStatement<*>, userToken: UserToken) {
        body[TokenTable.id] = userToken.id
        body[TokenTable.value] = userToken.value
        body[TokenTable.userId] = userToken.userId
        body[TokenTable.client] = userToken.client
        body[TokenTable.type] = userToken.type.name
        body[TokenTable.createdDate] = userToken.createdDate
    }
}