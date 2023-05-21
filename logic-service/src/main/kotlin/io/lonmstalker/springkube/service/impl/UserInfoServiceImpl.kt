package io.lonmstalker.springkube.service.impl

import io.lonmstalker.springkube.exception.ObjectNotFoundException
import io.lonmstalker.springkube.model.BotPositionInfo
import io.lonmstalker.springkube.model.BotUserInfo
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import io.lonmstalker.springkube.repository.UserInfoRepository
import io.lonmstalker.springkube.service.UserInfoService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserInfoServiceImpl(
    private val userInfoRepository: UserInfoRepository
) : UserInfoService {

    override suspend fun findAll(
        botId: UUID,
        userInfo: UserInfo,
        request: FilterRequest
    ): Pair<PageResponse, List<BotUserInfo>> = this.userInfoRepository.findAll(botId, request)

    override suspend fun findById(id: UUID, userInfo: UserInfo): BotUserInfo =
        this.userInfoRepository
            .findById(id, userInfo)
            ?: throw ObjectNotFoundException(id)

    override suspend fun save(user: BotUserInfo, userInfo: UserInfo): BotUserInfo =
        this.userInfoRepository.save(user)
            .apply { log.debug("saved user {} by {}", this.id, userInfo.userId) }

    override suspend fun update(user: BotUserInfo, userInfo: UserInfo): BotUserInfo =
        this.userInfoRepository.update(user)
            .apply { log.debug("updated user {} by {}", this.id, userInfo.userId) }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(UserInfoServiceImpl::class.java)
    }
}