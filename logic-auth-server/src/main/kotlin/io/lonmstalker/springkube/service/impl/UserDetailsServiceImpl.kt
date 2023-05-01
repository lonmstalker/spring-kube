package io.lonmstalker.springkube.service.impl

import io.lonmstalker.springkube.exception.BaseException
import io.lonmstalker.springkube.model.CustomUserDetails
import io.lonmstalker.springkube.service.PasswordService
import io.lonmstalker.springkube.service.UserInfoService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val userInfoService: UserInfoService,
    private val userPasswordService: PasswordService
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails =
        try {
            this.userInfoService
                .findByUsername(username)
                .let { CustomUserDetails(it, this.userPasswordService.findById(it.userPasswordId)) }
        } catch (ex: BaseException) {
            throw UsernameNotFoundException(ex.msg, ex)
        }
}