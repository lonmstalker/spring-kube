package io.lonmstalker.springkube.controller

import io.lonmstalker.springkube.api.BotActionApi
import io.lonmstalker.springkube.dto.BotActionDto
import io.lonmstalker.springkube.dto.FilterBotActionResponseDto
import io.lonmstalker.springkube.dto.FilterRequestDto
import io.lonmstalker.springkube.mapper.ActionMapper
import io.lonmstalker.springkube.mapper.FilterMapper
import io.lonmstalker.springkube.service.BotActionService
import io.lonmstalker.springkube.utils.UserUtils.getUser
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class BotActionController(
    private val actionMapper: ActionMapper,
    private val filterMapper: FilterMapper,
    private val botActionService: BotActionService
) : BotActionApi {

    override suspend fun findAll(filterRequestDto: FilterRequestDto): FilterBotActionResponseDto =
        this.botActionService
            .findAll(getUser(), this.filterMapper.toModel(filterRequestDto), true)
            .let {
                FilterBotActionResponseDto(filterMapper.toDto(it.first), it.second.map { this.actionMapper.toDto(it) })
            }

    override suspend fun findById(id: UUID): BotActionDto =
        this.botActionService
            .findById(id, getUser())
            .let { this.actionMapper.toDto(it) }

    override suspend fun save(action: BotActionDto): BotActionDto =
        this.botActionService
            .save(this.actionMapper.toModel(action), getUser())
            .let { this.actionMapper.toDto(it) }

    override suspend fun update(action: BotActionDto): BotActionDto =
        this.botActionService
            .update(this.actionMapper.toModel(action), getUser())
            .let { this.actionMapper.toDto(it) }

}