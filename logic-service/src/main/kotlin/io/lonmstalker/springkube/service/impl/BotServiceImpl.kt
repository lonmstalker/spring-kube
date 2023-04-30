package io.lonmstalker.springkube.service.impl

import io.lonmstalker.springkube.model.Bot
import io.lonmstalker.springkube.service.BotService
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import java.util.*

@Service
class BotServiceImpl : BotService {

    override fun getBots(userId: UUID): Flow<Bot> {
        TODO("Not yet implemented")
    }

    override suspend fun findById(id: UUID, userId: UUID): Bot {
        TODO("Not yet implemented")
    }
}