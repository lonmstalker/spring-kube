package io.lonmstalker.springkube.mapper

import io.lonmstalker.springkube.dto.BotDto
import io.lonmstalker.springkube.model.Bot
import io.lonmstalker.springkube.model.tables.records.BotRecord
import org.jooq.Record
import org.jooq.RecordMapper
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import java.util.*

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    imports = [UUID::class, BotDto.Status::class]
)
interface BotMapper : RecordMapper<Record, Bot> {

    fun fromRecord(bot: BotRecord?): Bot

    @Mapping(target = "userGroupId", ignore = true)
    @Mapping(target = "version", defaultValue = "0")
    @Mapping(target = "id", defaultExpression = "java(UUID.randomUUID())")
    @Mapping(target = "status", defaultExpression = "java(BotDto.Status.enabled.name())")
    fun toModel(bot: BotDto): Bot

    fun toDto(bot: Bot): BotDto

    override fun map(record: Record): Bot = this.fromRecord(record.into(BotRecord::class.java))
}