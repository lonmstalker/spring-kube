package io.lonmstalker.springkube.utils

import io.lonmstalker.springkube.model.RequestContext
import kotlinx.coroutines.reactive.awaitFirstOrNull
import reactor.core.publisher.Mono

object RequestUtils {

    @JvmStatic
    suspend fun getSuspendContext(): RequestContext? =
        this
            .getContext()
            .awaitFirstOrNull()

    @JvmStatic
    fun getContext(): Mono<RequestContext> =
        Mono
            .deferContextual { Mono.just(it.get(RequestContext::class.java)) }
}