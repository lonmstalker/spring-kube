package io.lonmstalker.springkube.utils.internal

import io.lonmstalker.springkube.helper.ReactiveMessageHelper
import io.lonmstalker.springkube.helper.ServletMessageHelper
import io.lonmstalker.springkube.model.system.FieldError
import io.lonmstalker.springkube.utils.internal.ExceptionUtils.getMessageByExchange
import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException
import org.springframework.web.server.ServerWebExchange

internal object ExceptionUtils {

    @JvmStatic
    fun ConstraintViolationException.toError(
        exchange: ServerWebExchange,
        messageHelper: ReactiveMessageHelper
    ): List<FieldError> =
        this.constraintViolations
            .map { mapFieldError(it, messageHelper.getMessageByExchange(it, exchange)) }

    @JvmStatic
    fun ConstraintViolationException.toError(messageHelper: ServletMessageHelper): List<FieldError> =
        this.constraintViolations
            .map { mapFieldError(it, messageHelper.getMessage(it)) }

    private fun ServletMessageHelper.getMessage( violation: ConstraintViolation<*>) =
        this.getMessage(
            violation.messageTemplate,
            violation.executableParameters,
            violation.message
        )

    private fun ReactiveMessageHelper.getMessageByExchange(
        violation: ConstraintViolation<*>,
        exchange: ServerWebExchange
    ) = this.getMessageByExchange(
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