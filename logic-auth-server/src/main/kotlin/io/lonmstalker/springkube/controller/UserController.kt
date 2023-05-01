package io.lonmstalker.springkube.controller

import io.lonmstalker.springkube.api.UserApi
import io.lonmstalker.springkube.dto.RegUserRequestDto
import io.lonmstalker.springkube.dto.UserInfoDto
import io.lonmstalker.springkube.mapper.UserMapper
import io.lonmstalker.springkube.service.UserInfoService
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class UserController(
    private val userMapper: UserMapper,
    private val userInfoService: UserInfoService
) : UserApi {

    override fun regUser(regUser: RegUserRequestDto): UserInfoDto =
        this.userMapper.toModel(regUser)
            .let { this.userInfoService.save(it, regUser.password) }
            .let { this.userMapper.toDto(it) }

    override fun regUserByInvite(id: UUID, regUser: RegUserRequestDto): UserInfoDto =
        this.userMapper.toModel(regUser, id)
            .let { this.userInfoService.save(it, regUser.password) }
            .let { this.userMapper.toDto(it) }
}