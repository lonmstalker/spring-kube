package io.lonmstalker.springkube.filter

import io.lonmstalker.springkube.model.system.RequestContext
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import org.springframework.web.server.i18n.LocaleContextResolver
import reactor.core.publisher.Mono
import java.util.*

class ReactiveRequestContextWebFilter(
    private val localeContextResolver: LocaleContextResolver
) : WebFilter {

    @PostConstruct
    fun init() {
        LoggerFactory.getLogger(ReactiveRequestContextWebFilter::class.java).info("${this::class.java} is enabled")
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> =
        chain.filter(exchange)
            .contextWrite { it.put(RequestContext::class.java, this.resolveRequestContext(exchange)) }

    private fun resolveRequestContext(exchange: ServerWebExchange): RequestContext =
        RequestContext(
            this.localeContextResolver.resolveLocaleContext(exchange).locale ?: Locale.getDefault(),
            exchange
        )
}