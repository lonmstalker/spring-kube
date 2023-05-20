package io.lonmstalker.springkube.service

import io.lonmstalker.springkube.model.BotUserInfo
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import java.util.*

interface UserInfoService {
    suspend fun findAll(
        userInfo: UserInfo,
        request: FilterRequest,
        onlyCurrentUser: Boolean
    ): Pair<PageResponse, List<BotUserInfo>>

    suspend fun findById(id: UUID, userInfo: UserInfo): BotUserInfo
    suspend fun save(bot: BotUserInfo, userInfo: UserInfo): BotUserInfo
    suspend fun update(bot: BotUserInfo, userInfo: UserInfo): BotUserInfo
}