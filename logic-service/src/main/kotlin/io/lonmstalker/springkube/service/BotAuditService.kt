package io.lonmstalker.springkube.service

import io.lonmstalker.springkube.model.BotActionAudit
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import java.util.*

interface BotAuditService {
    suspend fun findAll(
        userInfo: UserInfo,
        request: FilterRequest,
        onlyCurrentUser: Boolean
    ): Pair<PageResponse, List<BotActionAudit>>

    suspend fun findById(id: UUID, userInfo: UserInfo): BotActionAudit
    suspend fun save(bot: BotActionAudit, userInfo: UserInfo): BotActionAudit
    suspend fun update(bot: BotActionAudit, userInfo: UserInfo): BotActionAudit
}