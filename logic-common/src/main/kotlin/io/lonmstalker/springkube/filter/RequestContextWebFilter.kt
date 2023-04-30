package io.lonmstalker.springkube.filter

import io.lonmstalker.springkube.model.RequestContext
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import org.springframework.web.server.i18n.LocaleContextResolver
import reactor.core.publisher.Mono
import java.util.*

class RequestContextWebFilter(
    private val localeContextResolver: LocaleContextResolver
) : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> =
        chain.filter(exchange)
            .contextWrite { it.put(RequestContext::class.java, this.resolveRequestContext(exchange)) }

    private fun resolveRequestContext(exchange: ServerWebExchange): RequestContext =
        RequestContext(
            this.localeContextResolver.resolveLocaleContext(exchange).locale ?: Locale.getDefault(),
            exchange
        )
}