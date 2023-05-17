package io.lonmstalker.springkube.service

import io.lonmstalker.springkube.model.Bot
import java.util.UUID

interface TelegramBotService {
    fun enableBot(bot: Bot, userId: UUID)
    fun disableBot(bot: Bot, userId: UUID)
}