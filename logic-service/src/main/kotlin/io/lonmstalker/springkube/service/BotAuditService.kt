package io.lonmstalker.springkube.service

import io.lonmstalker.springkube.model.BotActionAudit
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import java.util.*

interface BotAuditService {
    suspend fun findAll(
        botId: UUID,
        userInfo: UserInfo,
        request: FilterRequest
    ): Pair<PageResponse, List<BotActionAudit>>

    suspend fun save(audit: BotActionAudit, userInfo: UserInfo): BotActionAudit
}