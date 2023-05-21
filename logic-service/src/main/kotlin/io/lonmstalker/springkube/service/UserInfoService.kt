package io.lonmstalker.springkube.service

import io.lonmstalker.springkube.model.BotUserInfo
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import java.util.*

interface UserInfoService {
    suspend fun findAll(botId: UUID, userInfo: UserInfo, request: FilterRequest): Pair<PageResponse, List<BotUserInfo>>

    suspend fun findById(id: UUID, userInfo: UserInfo): BotUserInfo
    suspend fun save(user: BotUserInfo, userInfo: UserInfo): BotUserInfo
    suspend fun update(user: BotUserInfo, userInfo: UserInfo): BotUserInfo
}