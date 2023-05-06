package io.lonmstalker.springkube.adapter

import io.lonmstalker.springkube.dto.TokenResponseDto

interface AuthWebAdapter {
    fun authenticate(requestBody: Map<String, String>): TokenResponseDto
}