package io.lonmstalker.springkube.model

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

data class CustomTokenAuthentication(
    internal val userInfo: UserInfo
) : AbstractAuthenticationToken(userInfo.authorities.map { GrantedAuthority { it } }) {
    override fun getCredentials(): Any = this.userInfo

    override fun getPrincipal(): Any = this.userInfo
}