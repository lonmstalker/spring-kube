package io.lonmstalker.springkube.repository

import io.lonmstalker.springkube.model.BotUserInfo
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import java.util.*

interface UserInfoRepository {
    suspend fun findAll(botId: UUID, request: FilterRequest): Pair<PageResponse, List<BotUserInfo>>
    suspend fun save(user: BotUserInfo): BotUserInfo
    suspend fun update(user: BotUserInfo): BotUserInfo
    suspend fun findById(id: UUID, userInfo: UserInfo): BotUserInfo?
}