package io.lonmstalker.springkube.checker

import io.lonmstalker.springkube.enums.UserStatus
import io.lonmstalker.springkube.model.system.CustomUserDetails
import io.lonmstalker.springkube.service.UserInfoService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsChecker
import org.springframework.stereotype.Component

@Component
class PostAuthenticationChecker(private val userInfoService: UserInfoService) : UserDetailsChecker {

    override fun check(toCheck: UserDetails) {
        val user = (toCheck as CustomUserDetails).user
        if (user.status == UserStatus.BLOCKED) {
            this.userInfoService.updateStatus(user.id, UserStatus.ACTIVATED)
        }
    }
}