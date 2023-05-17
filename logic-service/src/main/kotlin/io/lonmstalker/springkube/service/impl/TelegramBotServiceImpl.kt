package io.lonmstalker.springkube.service.impl

import io.lonmstalker.springkube.bot.TelegramLongPollingBotImpl
import io.lonmstalker.springkube.model.Bot
import io.lonmstalker.springkube.service.BotWorkerService
import io.lonmstalker.springkube.service.TelegramBotService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.DefaultAbsSender
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService

@Service
class TelegramBotServiceImpl(
    private val executorService: ExecutorService,
    private val botWorkerService: BotWorkerService,
) : TelegramBotService {
    private val exeField = executorField()
    private val botMap = ConcurrentHashMap<UUID, TelegramLongPollingBot>()

    override fun enableBot(bot: Bot, userId: UUID) {
        if (!this.botMap.contains(bot.id)) {
            val createdBot = TelegramLongPollingBotImpl(bot, botWorkerService)
            exeField.set(createdBot, this.executorService)

            this.botMap[bot.id] = createdBot
            log.info("bot {} enabled by {}", bot.id, userId)
        }
    }

    override fun disableBot(bot: Bot, userId: UUID) {
        this.botMap.remove(bot.id)?.onClosing()
        log.info("bot {} disabled by {}", bot.id, userId)
    }

    private fun executorField() = DefaultAbsSender::class.java.getDeclaredField("exe")

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(TelegramBotServiceImpl::class.java)
    }
}