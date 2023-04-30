package io.lonmstalker.springkube.repository.impl

import io.lonmstalker.springkube.helper.ClockHelper
import io.lonmstalker.springkube.mapper.BotMapper
import io.lonmstalker.springkube.model.Bot
import io.lonmstalker.springkube.model.tables.references.BOT
import io.lonmstalker.springkube.repository.BotRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*

@Repository
class BotRepositoryImpl(
    private val ctx: DSLContext,
    private val botMapper: BotMapper,
    private val clockHelper: ClockHelper,
) : BotRepository {

    override fun findBots(userId: UUID): Flow<Bot> =
        this.ctx
            .selectFrom(BOT)
            .where(BOT.CREATED_BY.eq(userId))
            .run { Mono.from(this) }
            .asFlow()
            .map { this.botMapper.fromRecord(it) }

    override fun findByUserGroup(userGroupId: UUID): Flow<Bot> =
        this.ctx
            .selectFrom(BOT)
            .where(BOT.USER_GROUP_ID.eq(userGroupId))
            .run { Mono.from(this) }
            .asFlow()
            .map { this.botMapper.fromRecord(it) }
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