package io.lonmstalker.springkube.mapper

import io.lonmstalker.springkube.config.MapstructConfig
import io.lonmstalker.springkube.dto.BotPositionInfoDto
import io.lonmstalker.springkube.model.BotPositionInfo
import io.lonmstalker.springkube.model.tables.records.BotPositionInfoRecord
import org.jooq.Record
import org.jooq.RecordMapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(config = MapstructConfig::class)
interface PositionMapper : RecordMapper<Record, BotPositionInfo> {

    fun fromRecord(bot: BotPositionInfoRecord?): BotPositionInfo

    @Mapping(target = "userGroupId", ignore = true)
    fun toModel(position: BotPositionInfoDto): BotPositionInfo

    fun toDto(position: BotPositionInfo): BotPositionInfoDto

    override fun map(record: Record): BotPositionInfo? =
        this.fromRecord(record.into(BotPositionInfoRecord::class.java))
}