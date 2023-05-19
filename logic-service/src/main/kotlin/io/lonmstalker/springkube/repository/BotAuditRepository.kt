package io.lonmstalker.springkube.repository

import io.lonmstalker.springkube.model.BotActionAudit
import io.lonmstalker.springkube.model.UserInfo

interface BotAuditRepository {
    suspend fun save(action: BotActionAudit, userInfo: UserInfo): BotActionAudit
}