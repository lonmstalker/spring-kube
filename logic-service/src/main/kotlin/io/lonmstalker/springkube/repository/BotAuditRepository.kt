package io.lonmstalker.springkube.repository

import io.lonmstalker.springkube.model.BotActionAudit
import io.lonmstalker.springkube.model.BotPositionInfo
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import java.util.*

interface BotAuditRepository {
    suspend fun findAll(botId: UUID, request: FilterRequest): Pair<PageResponse, List<BotActionAudit>>
    suspend fun save(action: BotActionAudit, userInfo: UserInfo): BotActionAudit
}