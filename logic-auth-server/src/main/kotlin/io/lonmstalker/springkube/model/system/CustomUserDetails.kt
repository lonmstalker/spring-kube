package io.lonmstalker.springkube.model.system

import io.lonmstalker.springkube.enums.UserStatus
import io.lonmstalker.springkube.model.User
import io.lonmstalker.springkube.model.UserPassword
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
    internal val user: User,
    private val userPassword: UserPassword
) : UserDetails {
    private val authorities = mutableSetOf(SimpleGrantedAuthority(user.role.name))

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities

    override fun getPassword(): String = userPassword.value

    override fun getUsername(): String = user.username

    override fun isAccountNonExpired(): Boolean = user.status != UserStatus.EXPIRED

    override fun isAccountNonLocked(): Boolean = user.status != UserStatus.BLOCKED

    override fun isCredentialsNonExpired(): Boolean = user.status != UserStatus.CREDENTIALS_EXPIRED

    override fun isEnabled(): Boolean  = user.status == UserStatus.ACTIVATED
}