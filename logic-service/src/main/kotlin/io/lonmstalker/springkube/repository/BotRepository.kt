package io.lonmstalker.springkube.repository

import io.lonmstalker.springkube.model.Bot
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import java.util.*

interface BotRepository {
    suspend fun findBots(userId: UUID, request: FilterRequest): Pair<PageResponse, List<Bot>>
    suspend fun findByUserGroup(userGroupId: UUID, request: FilterRequest): Pair<PageResponse, List<Bot>>
    suspend fun save(bot: Bot, userId: UUID, userGroupId: UUID): Bot
    suspend fun findById(id: UUID, userId: UUID, userGroupId: UUID): Bot?
}