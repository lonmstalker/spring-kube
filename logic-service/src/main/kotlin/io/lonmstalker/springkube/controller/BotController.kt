package io.lonmstalker.springkube.controller

import io.lonmstalker.springkube.utils.UserUtils
import io.lonmstalker.springkube.api.BotApi
import io.lonmstalker.springkube.dto.BotDto
import io.lonmstalker.springkube.mapper.BotMapper
import io.lonmstalker.springkube.service.BotService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class BotController(
    private val botMapper: BotMapper,
    private val botService: BotService,
) : BotApi {

    override suspend fun findById(id: UUID): BotDto =
        this.botService
            .findById(id, UserUtils.getUser())
            .let { this.botMapper.toDto(it) }

    override fun getBots(): Flow<BotDto> =
        this.botService
            .getBots(UserUtils.getUser(), true)
            .map { this.botMapper.toDto(it) }

    override suspend fun saveBot(bot: BotDto): BotDto =
        this.botService
            .save(this.botMapper.toModel(bot), UserUtils.getUser())
            .let { this.botMapper.toDto(it) }


    override suspend fun updateBot(bot: BotDto): BotDto =
        this.botService
            .update(this.botMapper.toModel(bot), UserUtils.getUser())
            .let { this.botMapper.toDto(it) }
}