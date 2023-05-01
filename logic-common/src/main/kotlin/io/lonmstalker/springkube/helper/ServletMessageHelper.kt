package io.lonmstalker.springkube.helper

import io.lonmstalker.springkube.exception.BaseException
import io.lonmstalker.springkube.utils.internal.ThreadLocalStorage
import org.springframework.context.MessageSource

class ServletMessageHelper(private val messageSource: MessageSource) {

    fun getMessage(code: String, args: Array<Any>? = null, defaultMessage: String? = null) =
        ThreadLocalStorage.getLocal()
            .let { this.messageSource.getMessage(code, args, defaultMessage, it) }

    fun getMessage(ex: BaseException) = this.getMessage(ex.code, ex.args, ex.message)
}