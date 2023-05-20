package io.lonmstalker.springkube.service.impl

import io.lonmstalker.springkube.model.BotAction
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import io.lonmstalker.springkube.service.BotActionService
import org.springframework.stereotype.Service
import java.util.*

@Service
class BotActionServiceImpl : BotActionService {

    override suspend fun findAll(
        userInfo: UserInfo,
        request: FilterRequest,
        onlyCurrentUser: Boolean
    ): Pair<PageResponse, List<BotAction>> {
        TODO("Not yet implemented")
    }

    override suspend fun findById(id: UUID, userInfo: UserInfo): BotAction {
        TODO("Not yet implemented")
    }

    override suspend fun save(bot: BotAction, userInfo: UserInfo): BotAction {
        TODO("Not yet implemented")
    }

    override suspend fun update(bot: BotAction, userInfo: UserInfo): BotAction {
        TODO("Not yet implemented")
    }
}