package io.lonmstalker.springkube.service

import io.lonmstalker.springkube.model.Bot
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import java.util.*

interface BotService {
    suspend fun findAll(userInfo: UserInfo, request: FilterRequest, onlyCurrentUser: Boolean): Pair<PageResponse, List<Bot>>
    suspend fun findById(id: UUID, userInfo: UserInfo): Bot
    suspend fun save(bot: Bot, userInfo: UserInfo): Bot
    suspend fun update(bot: Bot, userInfo: UserInfo): Bot
}