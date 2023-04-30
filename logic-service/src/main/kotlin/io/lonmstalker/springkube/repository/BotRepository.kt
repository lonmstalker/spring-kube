package io.lonmstalker.springkube.repository

import io.lonmstalker.springkube.model.Bot

interface BotRepository {
    suspend fun save(bot: Bot): Bot
}