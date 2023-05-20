package io.lonmstalker.springkube.service.impl

import io.lonmstalker.springkube.model.BotPositionInfo
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import io.lonmstalker.springkube.service.BotPositionService
import org.springframework.stereotype.Service
import java.util.*

@Service
class BotPositionServiceImpl : BotPositionService {

    override suspend fun findAll(
        userInfo: UserInfo,
        request: FilterRequest,
        onlyCurrentUser: Boolean
    ): Pair<PageResponse, List<BotPositionInfo>> {
        TODO("Not yet implemented")
    }

    override suspend fun findById(id: UUID, userInfo: UserInfo): BotPositionInfo {
        TODO("Not yet implemented")
    }

    override suspend fun save(bot: BotPositionInfo, userInfo: UserInfo): BotPositionInfo {
        TODO("Not yet implemented")
    }

    override suspend fun update(bot: BotPositionInfo, userInfo: UserInfo): BotPositionInfo {
        TODO("Not yet implemented")
    }
}