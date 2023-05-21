package io.lonmstalker.springkube.controller

import io.lonmstalker.springkube.api.TelegramUserApi
import io.lonmstalker.springkube.dto.FilterBotUserInfoResponseDto
import io.lonmstalker.springkube.dto.FilterRequestDto
import io.lonmstalker.springkube.mapper.FilterMapper
import io.lonmstalker.springkube.mapper.UserInfoMapper
import io.lonmstalker.springkube.service.UserInfoService
import io.lonmstalker.springkube.utils.UserUtils.getUser
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class TelegramUserInfoController(
    private val filterMapper: FilterMapper,
    private val userInfoMapper: UserInfoMapper,
    private val userInfoService: UserInfoService,
) : TelegramUserApi {

    override suspend fun findAll(botId: UUID, filterRequestDto: FilterRequestDto): FilterBotUserInfoResponseDto =
        this.userInfoService
            .findAll(botId, getUser(), this.filterMapper.toModel(filterRequestDto))
            .let {
                FilterBotUserInfoResponseDto(
                    filterMapper.toDto(it.first),
                    it.second.map { this.userInfoMapper.toDto(it) })
            }
}