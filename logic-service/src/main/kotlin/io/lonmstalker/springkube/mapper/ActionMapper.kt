package io.lonmstalker.springkube.mapper

import io.lonmstalker.springkube.model.BotAction
import io.lonmstalker.springkube.model.tables.records.BotActionRecord
import org.jooq.Record
import org.jooq.RecordMapper
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
interface ActionMapper : RecordMapper<Record, BotAction> {

    fun fromRecord(bot: BotActionRecord?): BotAction

    override fun map(record: Record): BotAction? =
        this.fromRecord(record.into(BotActionRecord::class.java))
}