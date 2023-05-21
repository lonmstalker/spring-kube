package io.lonmstalker.springkube.repository.impl

import io.lonmstalker.springkube.helper.ClockHelper
import io.lonmstalker.springkube.helper.JooqHelper
import io.lonmstalker.springkube.mapper.AuditMapper
import io.lonmstalker.springkube.model.BotActionAudit
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.model.paging.Filter
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.Operation
import io.lonmstalker.springkube.model.paging.PageResponse
import io.lonmstalker.springkube.model.tables.references.BOT
import io.lonmstalker.springkube.model.tables.references.BOT_ACTION_AUDIT
import io.lonmstalker.springkube.model.tables.references.BOT_POSITION_INFO
import io.lonmstalker.springkube.repository.BotAuditRepository
import kotlinx.coroutines.reactive.awaitFirst
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class BotAuditRepositoryImpl(
    private val ctx: DSLContext,
    private val jooqHelper: JooqHelper,
    private val clockHelper: ClockHelper,
    private val auditMapper: AuditMapper,
) : BotAuditRepository {
    private val joinBotId = "positionId[botPositionInfo].botId"

    override suspend fun findAll(botId: UUID, request: FilterRequest): Pair<PageResponse, List<BotActionAudit>> =
        jooqHelper.selectFluxWithCount(
            BOT_ACTION_AUDIT,
            request.apply { request.filters.add(Filter(joinBotId, listOf(botId), Operation.EQUALS)) },
            { auditMapper.map(it) },
            BOT_POSITION_INFO.BOT_ID.eq(botId)
        )

    override suspend fun save(action: BotActionAudit, userInfo: UserInfo): BotActionAudit =
        this.ctx
            .insertInto(
                BOT_ACTION_AUDIT,
                BOT_ACTION_AUDIT.ID,
                BOT_ACTION_AUDIT.POSITION_ID,
                BOT_ACTION_AUDIT.USER_ID,
                BOT_ACTION_AUDIT.CREATED_DATE,
            )
            .values(
                action.id,
                action.positionId,
                userInfo.userId,
                this.clockHelper.clockOffset(),
            )
            .returning()
            .awaitFirst()
            .map { this.auditMapper.map(it) }
}