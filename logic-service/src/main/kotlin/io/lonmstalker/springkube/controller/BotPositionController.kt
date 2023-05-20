package io.lonmstalker.springkube.controller

import io.lonmstalker.springkube.api.BotPositionApi
import io.lonmstalker.springkube.dto.BotPositionInfoDto
import io.lonmstalker.springkube.dto.FilterBotPositionInfoResponseDto
import io.lonmstalker.springkube.dto.FilterRequestDto
import io.lonmstalker.springkube.mapper.FilterMapper
import io.lonmstalker.springkube.mapper.PositionMapper
import io.lonmstalker.springkube.service.BotPositionService
import io.lonmstalker.springkube.utils.UserUtils.getUser
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class BotPositionController(
    private val filterMapper: FilterMapper,
    private val positionMapper: PositionMapper,
    private val botPositionService: BotPositionService

) : BotPositionApi {
    override suspend fun findAll(filterRequestDto: FilterRequestDto): FilterBotPositionInfoResponseDto =
        this.botPositionService
            .findAll(getUser(), this.filterMapper.toModel(filterRequestDto), true)
            .let {
                FilterBotPositionInfoResponseDto(filterMapper.toDto(it.first), it.second.map { this.positionMapper.toDto(it) })
            }

    override suspend fun findById(id: UUID): BotPositionInfoDto =
        this.botPositionService
            .findById(id, getUser())
            .let { this.positionMapper.toDto(it) }

    override suspend fun save(position: BotPositionInfoDto): BotPositionInfoDto =
        this.botPositionService
            .save(this.positionMapper.toModel(position), getUser())
            .let { this.positionMapper.toDto(it) }

    override suspend fun update(position: BotPositionInfoDto): BotPositionInfoDto =
        this.botPositionService
            .update(this.positionMapper.toModel(position), getUser())
            .let { this.positionMapper.toDto(it) }
}