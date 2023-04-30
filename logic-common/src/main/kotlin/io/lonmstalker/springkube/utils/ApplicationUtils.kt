package io.lonmstalker.springkube.utils

import reactor.core.publisher.Hooks

object ApplicationUtils {
    @JvmStatic
    fun init() {
        Hooks.enableAutomaticContextPropagation()
        System.setProperty("reactor.netty.http.server.accessLogEnabled", "true")
    }
}