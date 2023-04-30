package io.lonmstalker.springkube.model.system

import org.springframework.web.server.ServerWebExchange
import java.util.Locale

data class RequestContext(
    val locale: Locale,
    val rq: ServerWebExchange,
)