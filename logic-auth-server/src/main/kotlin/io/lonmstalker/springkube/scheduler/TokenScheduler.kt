package io.lonmstalker.springkube.scheduler

import io.lonmstalker.springkube.service.TokenService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class TokenScheduler(
    private val tokenService: TokenService
) {

    @Scheduled(cron = "\${schedule.token.clear}")
    fun cleanTokens() {
        this.tokenService.cleanExpiredTokens()
        log.info("token cleaned")
    }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(TokenScheduler::class.java)
    }
}