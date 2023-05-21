package io.lonmstalker.springkube.mapper

import io.lonmstalker.springkube.config.MapstructConfig
import io.lonmstalker.springkube.dto.BotUserInfoDto
import io.lonmstalker.springkube.enums.UserStatus
import io.lonmstalker.springkube.model.BotUserInfo
import io.lonmstalker.springkube.model.tables.records.BotUserInfoRecord
import org.jooq.Record
import org.jooq.RecordMapper
import org.mapstruct.Mapper

@Mapper(config = MapstructConfig::class)
interface UserInfoMapper : RecordMapper<Record, BotUserInfo> {

    fun fromRecord(bot: BotUserInfoRecord?): BotUserInfo

    fun toDto(userInfo: BotUserInfo): BotUserInfoDto

    override fun map(record: Record): BotUserInfo =
        this.fromRecord(record.into(BotUserInfoRecord::class.java))

    fun map(status: UserStatus): BotUserInfoDto.Status =
        when (status) {
            UserStatus.ACTIVE -> BotUserInfoDto.Status.active
            UserStatus.BLOCKED -> BotUserInfoDto.Status.blocked
            UserStatus.DELETED -> BotUserInfoDto.Status.deleted
        }

    fun map(status: BotUserInfoDto.Status): UserStatus =
        when (status) {
            BotUserInfoDto.Status.active -> UserStatus.ACTIVE
            BotUserInfoDto.Status.blocked -> UserStatus.BLOCKED
            BotUserInfoDto.Status.deleted -> UserStatus.DELETED
        }
}