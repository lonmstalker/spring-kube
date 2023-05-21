package io.lonmstalker.springkube.repository.impl

import io.lonmstalker.springkube.helper.ClockHelper
import io.lonmstalker.springkube.helper.JooqHelper
import io.lonmstalker.springkube.mapper.PositionMapper
import io.lonmstalker.springkube.model.BotPositionInfo
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import io.lonmstalker.springkube.model.tables.references.BOT_ACTION
import io.lonmstalker.springkube.model.tables.references.BOT_POSITION_INFO
import io.lonmstalker.springkube.repository.BotPositionRepository
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.jooq.Condition
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*

@Repository
class BotPositionRepositoryImpl(
    private val ctx: DSLContext,
    private val jooqHelper: JooqHelper,
    private val clockHelper: ClockHelper,
    private val positionMapper: PositionMapper,
) : BotPositionRepository {

    override suspend fun findAllByUser(
        userId: UUID,
        request: FilterRequest
    ): Pair<PageResponse, List<BotPositionInfo>> =
        request.selectFluxWithCount(BOT_POSITION_INFO.CREATED_BY.eq(userId))

    override suspend fun findAllByGroup(
        userGroupId: UUID,
        request: FilterRequest
    ): Pair<PageResponse, List<BotPositionInfo>> =
        request.selectFluxWithCount(BOT_POSITION_INFO.USER_GROUP_ID.eq(userGroupId))

    override suspend fun save(position: BotPositionInfo, userInfo: UserInfo): BotPositionInfo =
        this.ctx
            .insertInto(
                BOT_POSITION_INFO,
                BOT_POSITION_INFO.ID,
                BOT_POSITION_INFO.TITLE,
                BOT_POSITION_INFO.FROM_POSITION,
                BOT_POSITION_INFO.TO_POSITION,
                BOT_POSITION_INFO.BOT_ID,
                BOT_POSITION_INFO.ACTION_ID,
                BOT_POSITION_INFO.LOCALE,
                BOT_POSITION_INFO.USER_GROUP_ID,
                BOT_POSITION_INFO.CREATED_DATE,
                BOT_POSITION_INFO.CREATED_BY,
                BOT_POSITION_INFO.VERSION
            )
            .values(
                position.id,
                position.title,
                position.fromPosition,
                position.toPosition,
                position.botId,
                position.actionId,
                position.locale,
                userInfo.userGroupId,
                this.clockHelper.clockOffset(),
                userInfo.userId,
                0
            )
            .returning()
            .awaitFirst()
            .map { this.positionMapper.map(it) }

    override suspend fun update(position: BotPositionInfo, userInfo: UserInfo): BotPositionInfo =
        this.ctx
            .update(BOT_POSITION_INFO)
            .set(BOT_POSITION_INFO.TITLE, position.title)
            .set(BOT_POSITION_INFO.FROM_POSITION, position.fromPosition)
            .set(BOT_POSITION_INFO.TO_POSITION, position.toPosition)
            .set(BOT_POSITION_INFO.LOCALE, position.locale)
            .set(BOT_POSITION_INFO.VERSION, position.version + 1)
            .where(BOT_POSITION_INFO.ID.eq(position.id))
            .and(BOT_POSITION_INFO.CREATED_BY.eq(userInfo.userId).or(BOT_POSITION_INFO.USER_GROUP_ID.eq(userInfo.userGroupId)))
            .returning()
            .awaitFirst()
            .map { this.positionMapper.map(it) }

    override suspend fun findById(id: UUID, userInfo: UserInfo): BotPositionInfo? =
        this.ctx.selectFrom(BOT_POSITION_INFO)
            .where(BOT_POSITION_INFO.ID.eq(id))
            .and(BOT_POSITION_INFO.CREATED_BY.eq(userInfo.userId).or(BOT_POSITION_INFO.USER_GROUP_ID.eq(userInfo.userGroupId)))
            .run { Mono.from(this) }
            .awaitFirstOrNull()
            ?.map { this.positionMapper.map(it) }

    private suspend fun FilterRequest.selectFluxWithCount(condition: Condition) =
        jooqHelper.selectFluxWithCount(
            BOT_ACTION,
            this,
            { positionMapper.map(it) },
            condition
        )
}