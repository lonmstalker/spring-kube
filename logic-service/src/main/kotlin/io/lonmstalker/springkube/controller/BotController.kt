package io.lonmstalker.springkube.controller

import io.lonmstalker.springkube.api.BotApi
import io.lonmstalker.springkube.dto.BotDto
import io.lonmstalker.springkube.dto.BotsInnerDto
import io.lonmstalker.springkube.mapper.BotMapper
import io.lonmstalker.springkube.service.BotService
import kotlinx.coroutines.flow.Flow
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class BotController(
    private val botMapper: BotMapper,
    private val botService: BotService,
) : BotApi {

    override suspend fun findById(id: UUID): BotDto {
        TODO("Not yet implemented")
    }

    override fun getBots(): Flow<BotsInnerDto> {
        TODO("Not yet implemented")
    }
}