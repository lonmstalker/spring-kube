package io.lonmstalker.springkube.controller

import io.lonmstalker.springkube.adapter.AuthWebAdapter
import io.lonmstalker.springkube.api.AuthApi
import io.lonmstalker.springkube.dto.TokenResponseDto
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authWebAdapter: AuthWebAdapter
) : AuthApi {

    override fun authenticate(requestBody: Map<String, String>): TokenResponseDto =
        this.authWebAdapter.authenticate(requestBody)

    override fun authorize(requestBody: Map<String, String>) =
        this.authWebAdapter.authorize(requestBody)

    override fun login(requestBody: Map<String, String>) =
        this.authWebAdapter.login(requestBody)
}