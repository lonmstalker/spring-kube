package io.lonmstalker.springkube.service.impl

import io.lonmstalker.springkube.exception.ObjectNotFoundException
import io.lonmstalker.springkube.model.Bot
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.repository.BotRepository
import io.lonmstalker.springkube.service.BotService
import kotlinx.coroutines.flow.Flow
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class BotServiceImpl(
    private val botRepository: BotRepository
) : BotService {

    override fun getBots(userInfo: UserInfo, onlyCurrentUser: Boolean): Flow<Bot> =
        if (onlyCurrentUser) {
            this.botRepository.findBots(userInfo.userId)
        } else {
            this.botRepository.findByUserGroup(userInfo.userGroupId)
        }

    override suspend fun findById(id: UUID, userInfo: UserInfo): Bot =
        this.botRepository
            .findById(id, userInfo.userId, userInfo.userGroupId)
            ?: throw ObjectNotFoundException(id)

    override suspend fun save(bot: Bot, userInfo: UserInfo): Bot =
        this.save(bot, userInfo)
            .apply { log.debug("saved bot {} by {}", this.id, userInfo.userId) }

    override suspend fun update(bot: Bot, userInfo: UserInfo): Bot =
        this.save(bot, userInfo)
            .apply { log.debug("updated bot {} by {}", this.id, userInfo.userId) }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(BotServiceImpl::class.java)
    }
}