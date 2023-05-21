package io.lonmstalker.springkube.service.impl

import io.lonmstalker.springkube.model.BotActionAudit
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import io.lonmstalker.springkube.repository.BotAuditRepository
import io.lonmstalker.springkube.service.BotAuditService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class BotAuditServiceImpl(
    private val botAuditRepository: BotAuditRepository
) : BotAuditService {

    override suspend fun findAll(
        botId: UUID, userInfo: UserInfo, request: FilterRequest
    ): Pair<PageResponse, List<BotActionAudit>> = this.botAuditRepository.findAll(botId, request)

    override suspend fun save(audit: BotActionAudit, userInfo: UserInfo): BotActionAudit =
        this.botAuditRepository.save(audit, userInfo)
}