package io.lonmstalker.springkube.mapper

import io.lonmstalker.springkube.config.MapstructConfig
import io.lonmstalker.springkube.dto.BotDto
import io.lonmstalker.springkube.enums.BotStatus
import io.lonmstalker.springkube.model.Bot
import io.lonmstalker.springkube.model.tables.records.BotRecord
import org.jooq.Record
import org.jooq.RecordMapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import java.util.*

@Mapper(config = MapstructConfig::class, imports = [UUID::class])
interface BotMapper : RecordMapper<Record, Bot> {

    fun fromRecord(bot: BotRecord?): Bot

    @Mapping(target = "userGroupId", ignore = true)
    @Mapping(target = "version", defaultValue = "0")
    @Mapping(target = "id", defaultExpression = "java(UUID.randomUUID())")
    @Mapping(target = "status", defaultExpression = "java(BotStatus.ENABLED)")
    fun toModel(bot: BotDto): Bot

    fun toDto(bot: Bot): BotDto

    override fun map(record: Record): Bot = this.fromRecord(record.into(BotRecord::class.java))

    fun map(status: BotStatus): BotDto.Status =
        when (status) {
            BotStatus.ENABLED -> BotDto.Status.enabled
            BotStatus.DISABLED -> BotDto.Status.disabled
            BotStatus.BLOCKED -> BotDto.Status.blocked
        }

    fun map(status: BotDto.Status): BotStatus =
        when (status) {
            BotDto.Status.enabled -> BotStatus.ENABLED
            BotDto.Status.disabled -> BotStatus.DISABLED
            BotDto.Status.blocked -> BotStatus.BLOCKED
        }
}