package io.lonmstalker.springkube.model

import org.springframework.web.server.ServerWebExchange
import java.util.Locale

data class RequestContext(
    val locale: Locale,
    val rq: ServerWebExchange,
)