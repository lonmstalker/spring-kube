package io.lonmstalker.springkube.service

import io.lonmstalker.springkube.model.BotPositionInfo
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import java.util.*

interface BotPositionService {
    suspend fun findAll(
        userInfo: UserInfo,
        request: FilterRequest,
        onlyCurrentUser: Boolean
    ): Pair<PageResponse, List<BotPositionInfo>>

    suspend fun findById(id: UUID, userInfo: UserInfo): BotPositionInfo
    suspend fun save(position: BotPositionInfo, userInfo: UserInfo): BotPositionInfo
    suspend fun update(position: BotPositionInfo, userInfo: UserInfo): BotPositionInfo
}