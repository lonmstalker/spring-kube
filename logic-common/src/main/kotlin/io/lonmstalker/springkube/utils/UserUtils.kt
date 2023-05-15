package io.lonmstalker.springkube.utils

import io.lonmstalker.springkube.model.CustomTokenAuthentication
import io.lonmstalker.springkube.model.UserInfo
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import java.time.OffsetDateTime
import java.util.UUID

object UserUtils {

    @JvmStatic
    suspend fun getUser(): UserInfo =
        ReactiveSecurityContextHolder
            .getContext()
            .map {
                (it.authentication as CustomTokenAuthentication).userInfo
            }
            .awaitFirst()
}