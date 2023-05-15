package io.lonmstalker.springkube.utils

import reactor.core.publisher.Hooks
import reactor.netty.ReactorNetty

object ApplicationUtils {
    @JvmStatic
    fun init() {
        Hooks.enableAutomaticContextPropagation()
        System.setProperty(ReactorNetty.ACCESS_LOG_ENABLED, "true")
    }
}