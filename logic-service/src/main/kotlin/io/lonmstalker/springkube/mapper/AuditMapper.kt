package io.lonmstalker.springkube.mapper

import io.lonmstalker.springkube.config.MapstructConfig
import io.lonmstalker.springkube.dto.BotActionAuditDto
import io.lonmstalker.springkube.model.BotActionAudit
import io.lonmstalker.springkube.model.tables.records.BotActionAuditRecord
import org.jooq.Record
import org.jooq.RecordMapper
import org.mapstruct.Mapper

@Mapper(config = MapstructConfig::class)
interface AuditMapper : RecordMapper<Record, BotActionAudit> {

    fun fromRecord(bot: BotActionAuditRecord?): BotActionAudit

    fun toDto(audit: BotActionAudit): BotActionAuditDto

    override fun map(record: Record): BotActionAudit =
        this.fromRecord(record.into(BotActionAuditRecord::class.java))
}