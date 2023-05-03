package io.lonmstalker.springkube.provider

import io.lonmstalker.springkube.model.UserTokenInfo

interface TokenProvider {
    fun tryAuthenticate(request: Map<String, String>, client: String): UserTokenInfo?
}