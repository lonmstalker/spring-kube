package io.lonmstalker.springkube.controller

import io.lonmstalker.springkube.api.BotApi
import io.lonmstalker.springkube.dto.BotDto
import io.lonmstalker.springkube.dto.FilterBotResponseDto
import io.lonmstalker.springkube.dto.FilterRequestDto
import io.lonmstalker.springkube.mapper.BotMapper
import io.lonmstalker.springkube.mapper.FilterMapper
import io.lonmstalker.springkube.service.BotService
import io.lonmstalker.springkube.utils.UserUtils.getUser
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class BotController(
    private val botMapper: BotMapper,
    private val botService: BotService,
    private val filterMapper: FilterMapper
) : BotApi {

    override suspend fun findAll(filterRequestDto: FilterRequestDto): FilterBotResponseDto =
        this.botService
            .findAll(getUser(), this.filterMapper.toModel(filterRequestDto), true)
            .let { FilterBotResponseDto(filterMapper.toDto(it.first), it.second.map { this.botMapper.toDto(it) }) }

    override suspend fun findById(id: UUID): BotDto =
        this.botService
            .findById(id, getUser())
            .let { this.botMapper.toDto(it) }

    override suspend fun save(botDto: BotDto): BotDto =
        this.botService
            .save(this.botMapper.toModel(botDto), getUser())
            .let { this.botMapper.toDto(it) }

    override suspend fun update(botDto: BotDto): BotDto =
        this.botService
            .update(this.botMapper.toModel(botDto), getUser())
            .let { this.botMapper.toDto(it) }
}