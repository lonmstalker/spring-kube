package io.lonmstalker.springkube.repository.impl

import io.lonmstalker.springkube.helper.ClockHelper
import io.lonmstalker.springkube.helper.JooqHelper
import io.lonmstalker.springkube.mapper.BotMapper
import io.lonmstalker.springkube.model.Bot
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import io.lonmstalker.springkube.model.tables.references.BOT
import io.lonmstalker.springkube.repository.BotRepository
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class BotRepositoryImpl(
    private val ctx: DSLContext,
    private val botMapper: BotMapper,
    private val jooqHelper: JooqHelper,
    private val clockHelper: ClockHelper,
) : BotRepository {

    override suspend fun findBots(userId: UUID, request: FilterRequest): Pair<PageResponse, List<Bot>> =
        this.jooqHelper.selectFluxWithCount(
            BOT,
            request,
            { this.botMapper.fromRecord(it) },
            arrayOf(BOT.CREATED_BY.eq(userId))
        )

    override suspend fun findByUserGroup(userGroupId: UUID, request: FilterRequest): Pair<PageResponse, List<Bot>> =
        this.jooqHelper.selectFluxWithCount(
            BOT,
            request,
            { this.botMapper.fromRecord(it) },
            arrayOf(BOT.USER_GROUP_ID.eq(userGroupId))
        )

    override suspend fun save(bot: Bot, userId: UUID, userGroupId: UUID): Bot =
        this.ctx
            .insertInto(
                BOT,
                BOT.ID,
                BOT.TITLE,
                BOT.URL,
                BOT.HASH,
                BOT.STATUS,
                BOT.USER_GROUP_ID,
                BOT.CREATED_DATE,
                BOT.CREATED_BY,
                BOT.VERSION
            )
            .values(
                bot.id,
                bot.title,
                bot.url,
                bot.hash,
                bot.status,
                userGroupId,
                this.clockHelper.clockOffset(),
                userId,
                0
            )
            .returning()
            .awaitFirst()
            .map { this.botMapper.fromRecord(it) }

    override suspend fun findById(id: UUID, userId: UUID, userGroupId: UUID): Bot? =
        this.ctx
            .selectFrom(BOT)
            .where(BOT.ID.eq(id))
            .and(BOT.CREATED_BY.eq(userId).or(BOT.USER_GROUP_ID.eq(userGroupId)))
            .awaitFirstOrNull()
            ?.map { this.botMapper.fromRecord(it) }
}