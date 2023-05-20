package io.lonmstalker.springkube.service.impl

import io.lonmstalker.springkube.model.BotActionAudit
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import io.lonmstalker.springkube.service.BotAuditService
import org.springframework.stereotype.Service
import java.util.*

@Service
class BotAuditServiceImpl : BotAuditService {

    override suspend fun findAll(
        userInfo: UserInfo,
        request: FilterRequest,
        onlyCurrentUser: Boolean
    ): Pair<PageResponse, List<BotActionAudit>> {
        TODO("Not yet implemented")
    }

    override suspend fun findById(id: UUID, userInfo: UserInfo): BotActionAudit {
        TODO("Not yet implemented")
    }

    override suspend fun save(bot: BotActionAudit, userInfo: UserInfo): BotActionAudit {
        TODO("Not yet implemented")
    }

    override suspend fun update(bot: BotActionAudit, userInfo: UserInfo): BotActionAudit {
        TODO("Not yet implemented")
    }
}