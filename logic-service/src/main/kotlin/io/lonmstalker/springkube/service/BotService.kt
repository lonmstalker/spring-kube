package io.lonmstalker.springkube.service

import io.lonmstalker.springkube.model.Bot
import io.lonmstalker.springkube.model.UserInfo
import kotlinx.coroutines.flow.Flow
import java.util.*

interface BotService {
    fun getBots(userInfo: UserInfo, onlyCurrentUser: Boolean): Flow<Bot>
    suspend fun findById(id: UUID, userInfo: UserInfo): Bot
    suspend fun save(bot: Bot, userInfo: UserInfo): Bot
    suspend fun update(bot: Bot, userInfo: UserInfo): Bot
}