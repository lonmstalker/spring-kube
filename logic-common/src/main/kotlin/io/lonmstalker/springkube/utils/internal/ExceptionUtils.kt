package io.lonmstalker.springkube.utils.internal

import io.lonmstalker.springkube.helper.ReactiveMessageHelper
import io.lonmstalker.springkube.model.system.FieldError
import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException
import org.springframework.web.server.ServerWebExchange

object ExceptionUtils {

    @JvmStatic
    fun ConstraintViolationException.toError(
        exchange: ServerWebExchange,
        messageHelper: ReactiveMessageHelper
    ): List<FieldError> =
        this.constraintViolations
            .map { mapFieldError(it, messageHelper.getMessageByExchange(it, exchange)) }

    private fun ReactiveMessageHelper.getMessageByExchange(
        violation: ConstraintViolation<*>,
        exchange: ServerWebExchange
    ) =
        this.getMessageByExchange(
            exchange,
            violation.messageTemplate,
            violation.executableParameters,
            violation.message
        )

    private fun mapFieldError(violation: ConstraintViolation<*>, message: String?) =
        FieldError(
            violation.messageTemplate,
            violation.propertyPath.last().name,
            message
        )
}