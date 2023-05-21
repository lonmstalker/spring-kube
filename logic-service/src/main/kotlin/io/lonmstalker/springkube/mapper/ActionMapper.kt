package io.lonmstalker.springkube.mapper

import io.lonmstalker.springkube.config.MapstructConfig
import io.lonmstalker.springkube.dto.BotActionDto
import io.lonmstalker.springkube.model.BotAction
import io.lonmstalker.springkube.model.tables.records.BotActionRecord
import org.jooq.Record
import org.jooq.RecordMapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(config = MapstructConfig::class)
interface ActionMapper : RecordMapper<Record, BotAction> {

    fun fromRecord(bot: BotActionRecord?): BotAction

    @Mapping(target = "userGroupId", ignore = true)
    fun toModel(action: BotActionDto): BotAction

    fun toDto(action: BotAction): BotActionDto

    override fun map(record: Record): BotAction =
        this.fromRecord(record.into(BotActionRecord::class.java))
}