package io.lonmstalker.springkube.mapper

import io.lonmstalker.springkube.model.BotUserInfo
import io.lonmstalker.springkube.model.tables.records.BotUserInfoRecord
import org.jooq.Record
import org.jooq.RecordMapper
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
interface UserInfoMapper : RecordMapper<Record, BotUserInfo> {

    fun fromRecord(bot: BotUserInfoRecord?): BotUserInfo

    override fun map(record: Record): BotUserInfo? =
        this.fromRecord(record.into(BotUserInfoRecord::class.java))
}