package io.lonmstalker.springkube.helper

import io.lonmstalker.springkube.exception.BaseException
import io.lonmstalker.springkube.utils.RequestUtils
import org.springframework.context.MessageSource
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.i18n.LocaleContextResolver
import java.util.*

class ReactiveMessageHelper(
    private val messageSource: MessageSource,
    private val localeContextResolver: LocaleContextResolver
) {

    suspend fun getMessage(code: String, args: Array<Any>?, defaultMessage: String?) =
        this.resolveLocale()
            .let { this.messageSource.getMessage(code, args, defaultMessage, it) }

    suspend fun getMessage(ex: BaseException) = this.getMessage(ex.code, ex.args, ex.message)

    fun getMessageByExchange(
        exchange: ServerWebExchange,
        code: String,
        args: Array<Any>? = null,
        defaultMessage: String? = null,
    ): String? = this.messageSource
        .getMessage(code, args, defaultMessage, this.resolveExchangeLocal(exchange))

    fun getMessageByExchange(exchange: ServerWebExchange, ex: BaseException): String? =
        this.getMessageByExchange(exchange, ex.code, ex.args, ex.message)

    private suspend fun resolveLocale() =
        RequestUtils
            .getSuspendContext()
            ?.locale
            ?: Locale.getDefault()

    private fun resolveExchangeLocal(exchange: ServerWebExchange) =
        this.localeContextResolver
            .resolveLocaleContext(exchange)
            .locale
            ?: Locale.getDefault()
}