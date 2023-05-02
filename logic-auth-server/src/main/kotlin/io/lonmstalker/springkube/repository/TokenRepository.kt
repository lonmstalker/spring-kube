package io.lonmstalker.springkube.repository

import io.lonmstalker.springkube.enums.TokenType
import io.lonmstalker.springkube.model.UserToken
import java.util.*

interface TokenRepository {
    fun save(userToken: UserToken): UserToken
    fun saveAll(userTokens: Collection<UserToken>)
    fun findByTypeAndUserId(userId: UUID, type: TokenType): UserToken?
    fun deleteByClientAndUserId(userId: UUID, client: String): Int
}