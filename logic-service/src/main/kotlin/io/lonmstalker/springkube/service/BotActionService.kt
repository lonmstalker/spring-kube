package io.lonmstalker.springkube.service

import io.lonmstalker.springkube.model.BotAction
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import java.util.*

interface BotActionService {
    suspend fun findAll(
        userInfo: UserInfo,
        request: FilterRequest,
        onlyCurrentUser: Boolean
    ): Pair<PageResponse, List<BotAction>>

    suspend fun findById(id: UUID, userInfo: UserInfo): BotAction
    suspend fun save(action: BotAction, userInfo: UserInfo): BotAction
    suspend fun update(action: BotAction, userInfo: UserInfo): BotAction
}