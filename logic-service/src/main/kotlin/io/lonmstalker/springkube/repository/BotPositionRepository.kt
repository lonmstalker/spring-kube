package io.lonmstalker.springkube.repository

import io.lonmstalker.springkube.model.BotPositionInfo
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import java.util.*

interface BotPositionRepository {
    suspend fun findAllByUser(userId: UUID, request: FilterRequest): Pair<PageResponse, List<BotPositionInfo>>
    suspend fun findAllByGroup(userGroupId: UUID, request: FilterRequest): Pair<PageResponse, List<BotPositionInfo>>
    suspend fun save(position: BotPositionInfo, userInfo: UserInfo): BotPositionInfo
    suspend fun update(position: BotPositionInfo, userInfo: UserInfo): BotPositionInfo
    suspend fun findById(id: UUID, userInfo: UserInfo): BotPositionInfo?
}