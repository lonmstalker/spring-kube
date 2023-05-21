package io.lonmstalker.springkube.service.impl

import io.lonmstalker.springkube.exception.ObjectNotFoundException
import io.lonmstalker.springkube.model.Bot
import io.lonmstalker.springkube.model.BotAction
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import io.lonmstalker.springkube.repository.BotActionRepository
import io.lonmstalker.springkube.service.BotActionService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class BotActionServiceImpl(
    private val botActionRepository: BotActionRepository
) : BotActionService {

    override suspend fun findAll(
        userInfo: UserInfo, request: FilterRequest, onlyCurrentUser: Boolean
    ): Pair<PageResponse, List<BotAction>> =
        if (onlyCurrentUser) {
            this.botActionRepository.findAllByUser(userInfo.userId, request)
        } else {
            this.botActionRepository.findAllByGroup(userInfo.userGroupId, request)
        }

    override suspend fun findById(id: UUID, userInfo: UserInfo): BotAction =
        this.botActionRepository
            .findById(id, userInfo)
            ?: throw ObjectNotFoundException(id)

    override suspend fun save(action: BotAction, userInfo: UserInfo): BotAction =
        this.botActionRepository.save(action, userInfo)
            .apply { log.debug("saved action {} by {}", this.id, userInfo.userId) }

    override suspend fun update(action: BotAction, userInfo: UserInfo): BotAction =
        this.botActionRepository.update(action, userInfo)
            .apply { log.debug("updated action {} by {}", this.id, userInfo.userId) }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(BotActionServiceImpl::class.java)
    }
}