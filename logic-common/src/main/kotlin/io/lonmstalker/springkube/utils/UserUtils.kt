package io.lonmstalker.springkube.utils

import io.lonmstalker.springkube.model.UserInfo
import java.time.OffsetDateTime
import java.util.UUID

object UserUtils {

    @JvmStatic
    fun getUser(): UserInfo = UserInfo(UUID.randomUUID(), "", "", "", "", UUID.randomUUID(), OffsetDateTime.now())
}