package io.lonmstalker.springkube.provider

import io.lonmstalker.springkube.model.UserTokenInfo
import java.util.UUID

interface TokenProvider {
    fun getUserId(token: String): UUID
    fun tryAuthenticate(request: Map<String, String>, client: String): UserTokenInfo?
}