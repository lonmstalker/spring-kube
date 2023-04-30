package io.lonmstalker.springkube.utils

import java.util.UUID

object UserUtils {

    @JvmStatic
    fun getUserId(): UUID = UUID.randomUUID()
}