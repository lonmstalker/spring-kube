package io.lonmstalker.springkube.controller

import io.lonmstalker.springkube.api.AuthApi
import io.lonmstalker.springkube.provider.TokenProvider
import io.lonmstalker.springkube.constants.ErrorConstants.GRANT_TYPE_UNKNOWN
import io.lonmstalker.springkube.dto.TokenResponseDto
import io.lonmstalker.springkube.exception.AuthException
import io.lonmstalker.springkube.mapper.TokenMapper
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val tokenMapper: TokenMapper,
    private val tokenProviders: List<TokenProvider>
) : AuthApi {

    override fun authenticate(requestBody: Map<String, String>): TokenResponseDto {
        val client = SecurityContextHolder.getContext().authentication.principal as String
        return this.tokenProviders
            .asSequence()
            .map { it.tryAuthenticate(requestBody, client) }
            .firstOrNull()
            ?.let { this.tokenMapper.toDto(it) }
            ?: throw AuthException(GRANT_TYPE_UNKNOWN, "invalid request")
    }

}