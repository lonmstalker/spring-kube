package io.lonmstalker.springkube.repository

import io.lonmstalker.springkube.enums.TokenType
import io.lonmstalker.springkube.model.UserToken
import java.time.Instant
import java.util.*

interface TokenRepository {
    fun save(userToken: UserToken): UserToken
    fun saveAll(userTokens: Collection<UserToken>)
    fun findByValueAndType(value: String, type: String): UserToken?
    fun findByTypeAndUserId(userId: UUID, type: TokenType): UserToken?
    fun deleteByClientAndUserId(userId: UUID, client: String): Int
    fun cleanTokenExpiredAtBefore(issuedAt: Instant): Int
}