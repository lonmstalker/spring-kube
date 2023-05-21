package io.lonmstalker.springkube.repository.impl

import com.fasterxml.jackson.databind.ObjectMapper
import io.lonmstalker.springkube.helper.ClockHelper
import io.lonmstalker.springkube.helper.JooqHelper
import io.lonmstalker.springkube.mapper.ActionMapper
import io.lonmstalker.springkube.model.BotAction
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import io.lonmstalker.springkube.model.tables.references.BOT_ACTION
import io.lonmstalker.springkube.repository.BotActionRepository
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.JSONB
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*

@Repository
class BotActionRepositoryImpl(
    private val ctx: DSLContext,
    private val jooqHelper: JooqHelper,
    private val clockHelper: ClockHelper,
    private val objectMapper: ObjectMapper,
    private val actionMapper: ActionMapper,
) : BotActionRepository {

    override suspend fun findAllByUser(userId: UUID, request: FilterRequest): Pair<PageResponse, List<BotAction>> =
        request.selectFluxWithCount(BOT_ACTION.CREATED_BY.eq(userId))

    override suspend fun findAllByGroup(
        userGroupId: UUID,
        request: FilterRequest
    ): Pair<PageResponse, List<BotAction>> =
        request.selectFluxWithCount(BOT_ACTION.USER_GROUP_ID.eq(userGroupId))

    override suspend fun save(action: BotAction, userInfo: UserInfo): BotAction =
        this.ctx
            .insertInto(
                BOT_ACTION,
                BOT_ACTION.ID,
                BOT_ACTION.TITLE,
                BOT_ACTION.DATA,
                BOT_ACTION.LOCALE,
                BOT_ACTION.TYPE,
                BOT_ACTION.USER_GROUP_ID,
                BOT_ACTION.CREATED_DATE,
                BOT_ACTION.CREATED_BY,
                BOT_ACTION.VERSION
            )
            .values(
                action.id,
                action.title,
                JSONB.jsonbOrNull(this.objectMapper.writeValueAsString(action.data)),
                action.locale,
                action.type.name,
                userInfo.userGroupId,
                this.clockHelper.clockOffset(),
                userInfo.userId,
                0
            )
            .returning()
            .awaitFirst()
            .map { this.actionMapper.map(it) }

    override suspend fun update(action: BotAction, userInfo: UserInfo): BotAction =
        this.ctx
            .update(BOT_ACTION)
            .set(BOT_ACTION.TITLE, action.title)
            .set(BOT_ACTION.DATA, JSONB.jsonbOrNull(this.objectMapper.writeValueAsString(action.data)))
            .set(BOT_ACTION.LOCALE, action.locale)
            .set(BOT_ACTION.TYPE, action.type.name)
            .set(BOT_ACTION.VERSION, action.version + 1)
            .where(BOT_ACTION.ID.eq(action.id))
            .and(BOT_ACTION.CREATED_BY.eq(userInfo.userId).or(BOT_ACTION.USER_GROUP_ID.eq(userInfo.userGroupId)))
            .returning()
            .awaitFirst()
            .map { this.actionMapper.map(it) }

    override suspend fun findById(id: UUID, userInfo: UserInfo): BotAction? =
        this.ctx.selectFrom(BOT_ACTION)
            .where(BOT_ACTION.ID.eq(id))
            .and(BOT_ACTION.CREATED_BY.eq(userInfo.userId).or(BOT_ACTION.USER_GROUP_ID.eq(userInfo.userGroupId)))
            .run { Mono.from(this) }
            .awaitFirstOrNull()
            ?.map { this.actionMapper.map(it) }

    private suspend fun FilterRequest.selectFluxWithCount(condition: Condition) =
        jooqHelper.selectFluxWithCount(
            BOT_ACTION,
            this,
            { actionMapper.map(it) },
            condition
        )
}