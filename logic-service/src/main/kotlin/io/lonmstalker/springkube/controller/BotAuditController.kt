package io.lonmstalker.springkube.controller

import io.lonmstalker.springkube.api.AuditApi
import io.lonmstalker.springkube.dto.FilterBotActionAuditResponseDto
import io.lonmstalker.springkube.dto.FilterRequestDto
import io.lonmstalker.springkube.mapper.AuditMapper
import io.lonmstalker.springkube.mapper.FilterMapper
import io.lonmstalker.springkube.service.BotAuditService
import io.lonmstalker.springkube.utils.UserUtils.getUser
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class BotAuditController(
    private val auditMapper: AuditMapper,
    private val filterMapper: FilterMapper,
    private val botAuditService: BotAuditService
) : AuditApi {

    override suspend fun findAll(botId: UUID, filterRequestDto: FilterRequestDto): FilterBotActionAuditResponseDto =
        this.botAuditService
            .findAll(botId, getUser(), this.filterMapper.toModel(filterRequestDto))
            .let {
                FilterBotActionAuditResponseDto(
                    filterMapper.toDto(it.first),
                    it.second.map { this.auditMapper.toDto(it) })
            }

}