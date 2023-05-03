package io.lonmstalker.springkube.checker

import com.google.common.cache.CacheBuilder
import io.lonmstalker.springkube.config.properties.AppProperties
import io.lonmstalker.springkube.constants.AuthConstants.MAX_LOGIN_ATTEMPTS
import io.lonmstalker.springkube.constants.ErrorConstants.USER_IN_BLOCK
import io.lonmstalker.springkube.enums.UserStatus
import io.lonmstalker.springkube.exception.AuthException
import io.lonmstalker.springkube.model.CustomUserDetails
import io.lonmstalker.springkube.service.UserInfoService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsChecker
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class PreAuthenticationChecker(
    appProperties: AppProperties,
    private val userInfoService: UserInfoService
) : UserDetailsChecker {

    private val blockTime = appProperties.auth.blockedTime
    private val blockedUsersCache = CacheBuilder.newBuilder()
        .concurrencyLevel(4)
        .expireAfterAccess(blockTime, TimeUnit.SECONDS)
        .maximumSize(512)
        .build<String, Boolean>()

    override fun check(toCheck: UserDetails) {
        val user = (toCheck as CustomUserDetails).user
        if (blockedUsersCache.asMap().containsKey(user.username)) {
            throw AuthException(USER_IN_BLOCK, "user in block", arrayOf(TimeUnit.SECONDS.toSeconds(blockTime)))
        }
        if (user.loginAttempts > MAX_LOGIN_ATTEMPTS) {
            this.userInfoService.updateStatus(user.id, UserStatus.BLOCKED)
            throw AuthException(USER_IN_BLOCK, "user in block", arrayOf(TimeUnit.SECONDS.toSeconds(blockTime)))
        }
    }
}