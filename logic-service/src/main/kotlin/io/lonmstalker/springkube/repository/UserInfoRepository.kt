package io.lonmstalker.springkube.repository

import io.lonmstalker.springkube.model.BotUserInfo
import io.lonmstalker.springkube.model.UserInfo
import java.util.*

interface UserInfoRepository {
    suspend fun save(action: BotUserInfo, userInfo: UserInfo): BotUserInfo
    suspend fun findById(id: UUID, userInfo: UserInfo): BotUserInfo?
}