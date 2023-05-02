package io.lonmstalker.springkube.controller

import io.lonmstalker.springkube.api.AuthApi
import io.lonmstalker.springkube.dto.TokenResponseDto
import io.lonmstalker.springkube.mapper.TokenMapper
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(private val tokenMapper: TokenMapper) : AuthApi {

    override fun authenticate(grantType: String?, username: String?, password: String?): TokenResponseDto {
        // todo
        TODO("Not yet implemented")
    }
}