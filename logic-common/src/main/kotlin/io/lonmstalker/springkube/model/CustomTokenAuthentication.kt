package io.lonmstalker.springkube.model

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class CustomTokenAuthentication(
    val userInfo: UserInfo
) : AbstractAuthenticationToken(setOf(SimpleGrantedAuthority(userInfo.role))) {
    override fun getCredentials(): Any = this.userInfo

    override fun getPrincipal(): Any = this.userInfo

    override fun isAuthenticated(): Boolean = true
}