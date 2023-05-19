package io.lonmstalker.springkube.repository

import io.lonmstalker.springkube.model.BotAction
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import java.util.*

interface BotActionRepository {
    suspend fun findAllByUser(userId: UUID, request: FilterRequest): Pair<PageResponse, List<BotAction>>
    suspend fun findAllByGroup(userGroupId: UUID, request: FilterRequest): Pair<PageResponse, List<BotAction>>
    suspend fun save(action: BotAction, userInfo: UserInfo): BotAction
    suspend fun update(action: BotAction, userInfo: UserInfo): BotAction
    suspend fun findById(id: UUID, userInfo: UserInfo): BotAction?
}