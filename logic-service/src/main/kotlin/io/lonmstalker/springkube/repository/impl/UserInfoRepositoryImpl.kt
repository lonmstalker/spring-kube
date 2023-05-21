package io.lonmstalker.springkube.repository.impl

import io.lonmstalker.springkube.helper.ClockHelper
import io.lonmstalker.springkube.helper.JooqHelper
import io.lonmstalker.springkube.mapper.UserInfoMapper
import io.lonmstalker.springkube.model.BotUserInfo
import io.lonmstalker.springkube.model.UserInfo
import io.lonmstalker.springkube.model.paging.FilterRequest
import io.lonmstalker.springkube.model.paging.PageResponse
import io.lonmstalker.springkube.model.tables.references.BOT
import io.lonmstalker.springkube.model.tables.references.BOT_USER_INFO
import io.lonmstalker.springkube.repository.UserInfoRepository
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*

@Repository
class UserInfoRepositoryImpl(
    private val ctx: DSLContext,
    private val jooqHelper: JooqHelper,
    private val clockHelper: ClockHelper,
    private val userMapper: UserInfoMapper,
) : UserInfoRepository {

    override suspend fun findAll(botId: UUID, request: FilterRequest): Pair<PageResponse, List<BotUserInfo>> =
        jooqHelper.selectFluxWithCount(
            BOT_USER_INFO,
            request,
            { userMapper.map(it) },
            BOT_USER_INFO.BOT_ID.eq(botId)
        )
    override suspend fun save(user: BotUserInfo): BotUserInfo =
        this.ctx
            .insertInto(
                BOT_USER_INFO,
                BOT_USER_INFO.ID,
                BOT_USER_INFO.BOT_ID,
                BOT_USER_INFO.TELEGRAM_ID,
                BOT_USER_INFO.USERNAME,
                BOT_USER_INFO.EMAIL,
                BOT_USER_INFO.PHONE,
                BOT_USER_INFO.FULL_NAME,
                BOT_USER_INFO.CURRENT_LOCALE,
                BOT_USER_INFO.CURRENT_POSITION,
                BOT_USER_INFO.STATUS,
                BOT_USER_INFO.CREATED_DATE
            )
            .values(
                user.id,
                user.botId,
                user.telegramId,
                user.username,
                user.email,
                user.phone,
                user.fullName,
                user.currentLocale,
                user.currentPosition,
                user.status.name,
                this.clockHelper.clockOffset()
            )
            .returning()
            .awaitFirst()
            .map { this.userMapper.map(it) }

    override suspend fun update(user: BotUserInfo): BotUserInfo =
        this.ctx
            .update(BOT_USER_INFO)
            .set(BOT_USER_INFO.USERNAME, user.username)
            .set(BOT_USER_INFO.EMAIL, user.email)
            .set(BOT_USER_INFO.PHONE, user.phone)
            .set(BOT_USER_INFO.FULL_NAME, user.fullName)
            .set(BOT_USER_INFO.CURRENT_LOCALE, user.currentLocale)
            .where(BOT_USER_INFO.ID.eq(user.id))
            .returning()
            .awaitFirst()
            .map { this.userMapper.map(it) }

    override suspend fun findById(id: UUID, userInfo: UserInfo): BotUserInfo? =
        this.ctx.select()
            .from(BOT_USER_INFO)
            .innerJoin(BOT_USER_INFO).on(BOT_USER_INFO.BOT_ID.eq(BOT.ID))
            .where(BOT_USER_INFO.ID.eq(id))
            .and(BOT.USER_GROUP_ID.eq(userInfo.userGroupId))
            .run { Mono.from(this) }
            .awaitFirstOrNull()
            ?.map { this.userMapper.map(it) }
}