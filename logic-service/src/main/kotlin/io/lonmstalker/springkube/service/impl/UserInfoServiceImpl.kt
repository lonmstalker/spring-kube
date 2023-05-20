package io.lonmstalker.springkube.service.impl

import io.lonmstalker.springkube.model.BotUserInfo
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import io.lonmstalker.springkube.service.UserInfoService
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserInfoServiceImpl : UserInfoService {

    override suspend fun findAll(
        userInfo: UserInfo,
        request: FilterRequest,
        onlyCurrentUser: Boolean
    ): Pair<PageResponse, List<BotUserInfo>> {
        TODO("Not yet implemented")
    }

    override suspend fun findById(id: UUID, userInfo: UserInfo): BotUserInfo {
        TODO("Not yet implemented")
    }

    override suspend fun save(bot: BotUserInfo, userInfo: UserInfo): BotUserInfo {
        TODO("Not yet implemented")
    }

    override suspend fun update(bot: BotUserInfo, userInfo: UserInfo): BotUserInfo {
        TODO("Not yet implemented")
    }
}