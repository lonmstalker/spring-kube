package io.lonmstalker.springkube.bot

import io.lonmstalker.springkube.model.Bot
import io.lonmstalker.springkube.service.BotWorkerService
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update

class TelegramLongPollingBotImpl(
    private val bot: Bot,
    private val botWorkerService: BotWorkerService
) : TelegramLongPollingBot(bot.hash) {

    override fun getBotUsername(): String = this.bot.username

    override fun onUpdateReceived(update: Update) {
        val msg = update.message
        val userId = msg.chatId
        TODO("Not yet implemented")
    }
}