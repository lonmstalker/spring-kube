package io.lonmstalker.springkube.exception.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
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
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.http.HttpStatus
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.context.request.WebRequest

class ServletExceptionHandler(
    private val clockHelper: ClockHelper,
    private val objectMapper: ObjectMapper,
    private val messageHelper: ServletMessageHelper
) : DefaultErrorAttributes() {

    @PostConstruct
    fun init() {
        log.info("${this::class.java} is enabled")
    }

    override fun getErrorAttributes(webRequest: WebRequest, options: ErrorAttributeOptions): MutableMap<String, Any> {
        val ex = super.getError(webRequest)

        if (ex == null) {
            val status = this.getCode(webRequest)
            if (status != null) {
                setStatus(webRequest, 400)
                return this.convertResponse(buildErrorDto(null, status = status))
            }
        }

        if (ex is BaseException) {
            this.logException(ex, false)
            return withCode(webRequest, 400, ex.code, messageHelper.getMessage(ex))
        }
        if (ex is ConstraintViolationException) {
            this.logException(ex, false)
            setStatus(webRequest, 400)
            return this.convertResponse(buildErrorDto(ex.message, status = 400, fields = ex.toError(messageHelper)))
        }
        if (ex is ValidationException) {
            this.logException(ex, false)
            setStatus(webRequest, 400)
            return this.convertResponse(buildErrorDto(ex.message, status = 400))
        }
        if (ex is HttpMediaTypeNotSupportedException) {
            return withCode(
                webRequest,
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                messageHelper.getMessage(INVALID_MEDIA_TYPE),
                INVALID_MEDIA_TYPE
            )
        }

        this.logException(ex, true)
        return this.convertResponse(buildErrorDto())
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
        webRequest: WebRequest,
        status: Int,
        message: String?,
        code: String?
    ): MutableMap<String, Any> {
        setStatus(webRequest, status)
        return convertResponse(buildErrorDto(message, code ?: ErrorCodes.INTERNAL_SERVER_ERROR, status))
    }

    private fun setStatus(webRequest: WebRequest, status: Int) {
        webRequest.setAttribute("javax.servlet.error.status_code", HttpStatus.valueOf(status), 0)
    }

    private fun convertResponse(body: Any) = this.objectMapper.convertValue<MutableMap<String, Any>>(body)

    private fun logException(ex: Throwable, isError: Boolean) {
        if (isError || log.isDebugEnabled) {
            log.error("catch exception: ", ex)
        } else {
            log.debug("catch exception: {}", ex.message)
        }
    }

    private fun getCode(webRequest: WebRequest): Int? = webRequest.getAttribute("jakarta.servlet.error.status_code", 0) as? Int

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(ServletExceptionHandler::class.java)
    }
}