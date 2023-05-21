package io.lonmstalker.springkube.service.impl

import io.lonmstalker.springkube.exception.ObjectNotFoundException
import io.lonmstalker.springkube.model.BotAction
import io.lonmstalker.springkube.model.BotPositionInfo
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import io.lonmstalker.springkube.repository.BotPositionRepository
import io.lonmstalker.springkube.service.BotPositionService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class BotPositionServiceImpl(
    private val botPositionRepository: BotPositionRepository
) : BotPositionService {

    override suspend fun findAll(
        userInfo: UserInfo, request: FilterRequest, onlyCurrentUser: Boolean
    ): Pair<PageResponse, List<BotPositionInfo>> =
        if (onlyCurrentUser) {
            this.botPositionRepository.findAllByUser(userInfo.userId, request)
        } else {
            this.botPositionRepository.findAllByGroup(userInfo.userGroupId, request)
        }

    override suspend fun findById(id: UUID, userInfo: UserInfo): BotPositionInfo =
        this.botPositionRepository
            .findById(id, userInfo)
            ?: throw ObjectNotFoundException(id)

    override suspend fun save(position: BotPositionInfo, userInfo: UserInfo): BotPositionInfo =
        this.botPositionRepository.save(position, userInfo)
            .apply { log.debug("saved position {} by {}", this.id, userInfo.userId) }

    override suspend fun update(position: BotPositionInfo, userInfo: UserInfo): BotPositionInfo =
        this.botPositionRepository.update(position, userInfo)
            .apply { log.debug("updated position {} by {}", this.id, userInfo.userId) }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(BotPositionServiceImpl::class.java)
    }
}