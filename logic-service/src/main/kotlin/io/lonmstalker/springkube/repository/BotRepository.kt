package io.lonmstalker.springkube.repository

import io.lonmstalker.springkube.model.Bot
import kotlinx.coroutines.flow.Flow
import java.util.*

interface BotRepository {
    fun findBots(userId: UUID): Flow<Bot>
    fun findByUserGroup(userGroupId: UUID): Flow<Bot>
    suspend fun save(bot: Bot, userId: UUID, userGroupId: UUID): Bot
    suspend fun findById(id: UUID, userId: UUID, userGroupId: UUID): Bot?
}