package io.lonmstalker.springkube.model.oauth2

import io.lonmstalker.springkube.constants.AuthConstants.ACCESS_ROLE
import io.lonmstalker.springkube.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

class CustomOAuth2User(
    val user: User
) : OAuth2User {
    override fun getName(): String = this.user.username
    override fun getAttributes(): MutableMap<String, Any> = mutableMapOf()
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableSetOf(SimpleGrantedAuthority(ACCESS_ROLE))
}