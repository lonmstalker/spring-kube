package io.lonmstalker.springkube.checker

import io.lonmstalker.springkube.config.properties.AppProperties
import io.lonmstalker.springkube.constants.AuthConstants.MAX_LOGIN_ATTEMPTS
import io.lonmstalker.springkube.constants.ErrorConstants.USER_IN_BLOCK
import io.lonmstalker.springkube.exception.AuthNoRollbackException
import io.lonmstalker.springkube.helper.ClockHelper
import io.lonmstalker.springkube.model.system.CustomUserDetails
import io.lonmstalker.springkube.service.UserInfoService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsChecker
import org.springframework.stereotype.Component
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

@Component
class PreAuthenticationChecker(
    appProperties: AppProperties,
    private val clockHelper: ClockHelper,
    private val userInfoService: UserInfoService
) : UserDetailsChecker {
    private val userInBlockMsg = "user in block"
    private val blockTime = appProperties.auth.blockedTime

    override fun check(toCheck: UserDetails) {
        val user = (toCheck as CustomUserDetails).user
        val lockedTime = user.lastBlocked
        if (lockedTime != null) {
            if (lockedTime.isBefore(this.clockHelper.clock())) {
                this.userInfoService.unlockUser(user.id)
                return
            }
            val seconds =
                lockedTime.toEpochSecond(ZoneOffset.UTC) - this.clockHelper.clock().toEpochSecond(ZoneOffset.UTC)
            throw AuthNoRollbackException(
                USER_IN_BLOCK,
                this.userInBlockMsg,
                arrayOf(TimeUnit.SECONDS.toMinutes(seconds))
            )
        }
        if (user.loginAttempts > MAX_LOGIN_ATTEMPTS) {
            this.userInfoService.lockUser(user.id, this.clockHelper.clock().plusMinutes(this.blockTime))
            throw AuthNoRollbackException(USER_IN_BLOCK, this.userInBlockMsg, arrayOf(this.blockTime))
        }
    }
}