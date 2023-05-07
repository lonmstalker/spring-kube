package io.lonmstalker.springkube.adapter

import io.lonmstalker.springkube.dto.TokenResponseDto

interface AuthWebAdapter {
    fun login(requestBody: Map<String, String>)
    fun authorize(requestBody: Map<String, String>)
    fun authenticate(requestBody: Map<String, String>): TokenResponseDto
}