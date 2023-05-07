package io.lonmstalker.springkube.utils

import io.lonmstalker.springkube.model.system.ReactiveRequestContext
import kotlinx.coroutines.reactive.awaitFirstOrNull
import reactor.core.publisher.Mono

object RequestUtils {

    @JvmStatic
    suspend fun getSuspendContext(): ReactiveRequestContext? =
        this
            .getContext()
            .awaitFirstOrNull()

    @JvmStatic
    fun getContext(): Mono<ReactiveRequestContext> =
        Mono
            .deferContextual { Mono.just(it.get(ReactiveRequestContext::class.java)) }
}