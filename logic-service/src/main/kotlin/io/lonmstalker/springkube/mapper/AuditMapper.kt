package io.lonmstalker.springkube.mapper

import io.lonmstalker.springkube.model.BotActionAudit
import io.lonmstalker.springkube.model.tables.records.BotActionAuditRecord
import org.jooq.Record
import org.jooq.RecordMapper
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
interface AuditMapper : RecordMapper<Record, BotActionAudit> {

    fun fromRecord(bot: BotActionAuditRecord?): BotActionAudit

    override fun map(record: Record): BotActionAudit? =
        this.fromRecord(record.into(BotActionAuditRecord::class.java))
}