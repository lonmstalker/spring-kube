package io.lonmstalker.springkube.utils

import io.lonmstalker.springkube.model.UserInfo
import java.util.UUID

object UserUtils {

    @JvmStatic
    fun getUser(): UserInfo = UserInfo(UUID.randomUUID(), UUID.randomUUID())
}