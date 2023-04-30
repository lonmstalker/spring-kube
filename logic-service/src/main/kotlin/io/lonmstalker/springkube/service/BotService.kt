package io.lonmstalker.springkube.service

import io.lonmstalker.springkube.model.Bot
import kotlinx.coroutines.flow.Flow
import java.util.*

interface BotService {
    fun getBots(userId: UUID): Flow<Bot>
    suspend fun findById(id: UUID, userId: UUID): Bot
}