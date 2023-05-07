package io.lonmstalker.springkube.provider

import io.lonmstalker.springkube.enums.Provider
import io.lonmstalker.springkube.model.UserTokenInfo

interface TokenProvider {
    fun supportGrantType(provider: Provider): Boolean
    fun authenticate(request: Map<String, String>, client: String): UserTokenInfo?
}