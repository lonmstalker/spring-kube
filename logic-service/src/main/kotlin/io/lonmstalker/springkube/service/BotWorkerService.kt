package io.lonmstalker.springkube.service

import java.util.UUID

interface BotWorkerService {

    fun getUserInfo(botId: UUID, userId: UUID)
}