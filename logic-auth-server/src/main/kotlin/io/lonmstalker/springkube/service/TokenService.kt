package io.lonmstalker.springkube.service

import io.lonmstalker.springkube.enums.TokenType
import io.lonmstalker.springkube.model.CreateTokenSettings
import io.lonmstalker.springkube.model.User
import io.lonmstalker.springkube.model.UserToken
import io.lonmstalker.springkube.model.UserTokenInfo
import java.util.*

interface TokenService {

    /**
     * Парсит токен и возвращает идентификатор пользователя
     */
    fun parseToken(token: String): UUID
    fun findByValueAndType(value: String, type: TokenType): UserToken
    fun createToken(user: User, client: String, settings: CreateTokenSettings): UserTokenInfo
}