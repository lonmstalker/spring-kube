package io.lonmstalker.springkube.exception.handler

import io.lonmstalker.springkube.constants.ErrorCodes
import io.lonmstalker.springkube.constants.ErrorCodes.INVALID_MEDIA_TYPE
import io.lonmstalker.springkube.exception.BaseException
import io.lonmstalker.springkube.helper.ClockHelper
import io.lonmstalker.springkube.helper.ServletMessageHelper
import io.lonmstalker.springkube.model.system.ErrorDto
import io.lonmstalker.springkube.model.system.FieldError
import io.lonmstalker.springkube.utils.internal.ExceptionUtils.toError
import jakarta.annotation.PostConstruct
import jakarta.validation.ConstraintViolationException
import jakarta.validation.ValidationException
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.core.annotation.AnnotationUtils.getAnnotation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
class ServletExceptionHandler(
    private val clockHelper: ClockHelper,
    private val messageHelper: ServletMessageHelper
) {

    @PostConstruct
    fun init() {
        log.info("${this::class.java} is enabled")
    }

    @ExceptionHandler(Throwable::class)
    fun getErrorAttributes(ex: Throwable): ResponseEntity<ErrorDto> {
        val status = getAnnotation(ex.javaClass, ResponseStatus::class.java)

        if (status != null) {
            val code = status.code.value()
            return ResponseEntity.status(code).body(buildErrorDto(null, status = code))
        }

        if (ex is BaseException) {
            this.logException(ex, false)
            return withCode(400, messageHelper.getMessage(ex), ex.code)
        }
        if (ex is ConstraintViolationException) {
            this.logException(ex, false)
            return ResponseEntity.status(400)
                .body(buildErrorDto(ex.message, status = 400, fields = ex.toError(messageHelper)))
        }
        if (ex is ValidationException) {
            this.logException(ex, false)
            return ResponseEntity.status(400).body(buildErrorDto(ex.message, status = 400))
        }
        if (ex is HttpMediaTypeNotSupportedException) {
            return withCode(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                messageHelper.getMessage(INVALID_MEDIA_TYPE),
                INVALID_MEDIA_TYPE
            )
        }

        this.logException(ex, true)
        return ResponseEntity.internalServerError().body(buildErrorDto())
    }

    private fun buildErrorDto(
        message: String? = null,
        code: String = ErrorCodes.INTERNAL_SERVER_ERROR,
        status: Int = HttpStatus.INTERNAL_SERVER_ERROR.value(),
        fields: List<FieldError>? = null
    ) = ErrorDto(
        code = code,
        status = status,
        message = message,
        timestamp = clockHelper.clock(),
        data = fields
    )

    private fun withCode(
        status: Int,
        message: String?,
        code: String?
    ): ResponseEntity<ErrorDto> =
        ResponseEntity
            .status(status)
            .body(buildErrorDto(message, code ?: ErrorCodes.INTERNAL_SERVER_ERROR, status))


    private fun logException(ex: Throwable, isError: Boolean) {
        if (isError || log.isDebugEnabled) {
            log.error("catch exception: ", ex)
        } else {
            log.debug("catch exception: {}", ex.message)
        }
    }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(ServletExceptionHandler::class.java)
    }
}