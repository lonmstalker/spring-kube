package io.lonmstalker.springkube.service.impl

import io.lonmstalker.springkube.exception.ObjectNotFoundException
import io.lonmstalker.springkube.model.Bot
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import io.lonmstalker.springkube.repository.BotRepository
import io.lonmstalker.springkube.service.BotService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class BotServiceImpl(
    private val botRepository: BotRepository
) : BotService {

    override suspend fun getBots(
        userInfo: UserInfo, request: FilterRequest, onlyCurrentUser: Boolean
    ): Pair<PageResponse, List<Bot>> =
        if (onlyCurrentUser) {
            this.botRepository.findAllByUser(userInfo.userId, request)
        } else {
            this.botRepository.findAllByGroup(userInfo.userGroupId, request)
        }

    override suspend fun findById(id: UUID, userInfo: UserInfo): Bot =
        this.botRepository
            .findById(id, userInfo)
            ?: throw ObjectNotFoundException(id)

    override suspend fun save(bot: Bot, userInfo: UserInfo): Bot =
        this.botRepository.save(bot, userInfo)
            .apply { log.debug("saved bot {} by {}", this.id, userInfo.userId) }

    override suspend fun update(bot: Bot, userInfo: UserInfo): Bot =
        this.botRepository.save(bot, userInfo)
            .apply { log.debug("updated bot {} by {}", this.id, userInfo.userId) }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(BotServiceImpl::class.java)
    }
}