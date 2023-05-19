package io.lonmstalker.springkube.repository

import io.lonmstalker.springkube.model.Bot
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import java.util.*

interface BotRepository {
    suspend fun findAllByUser(userId: UUID, request: FilterRequest): Pair<PageResponse, List<Bot>>
    suspend fun findAllByGroup(userGroupId: UUID, request: FilterRequest): Pair<PageResponse, List<Bot>>
    suspend fun save(bot: Bot, userInfo: UserInfo): Bot
    suspend fun update(bot: Bot, userInfo: UserInfo): Bot
    suspend fun findById(id: UUID, userInfo: UserInfo): Bot?
}