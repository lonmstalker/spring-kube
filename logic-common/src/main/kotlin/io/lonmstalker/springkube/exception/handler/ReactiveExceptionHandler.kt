package io.lonmstalker.springkube.exception.handler

import com.fasterxml.jackson.databind.ObjectMapper
import io.lonmstalker.springkube.constants.ErrorCodes
import io.lonmstalker.springkube.constants.ErrorCodes.INVALID_MEDIA_TYPE
import io.lonmstalker.springkube.exception.BaseException
import io.lonmstalker.springkube.helper.ClockHelper
import io.lonmstalker.springkube.helper.ReactiveMessageHelper
import io.lonmstalker.springkube.model.ErrorDto
import io.lonmstalker.springkube.model.FieldError
import io.lonmstalker.springkube.utils.internal.ExceptionUtils.toError
import jakarta.annotation.PostConstruct
import jakarta.validation.ConstraintViolationException
import jakarta.validation.ValidationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.reactive.function.UnsupportedMediaTypeException
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebExceptionHandler
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class ReactiveExceptionHandler(
    private val clockHelper: ClockHelper,
    private val objectMapper: ObjectMapper,
    private val messageHelper: ReactiveMessageHelper
) : WebExceptionHandler {

    @PostConstruct
    fun init() {
        log.info("${this::class.java} is enabled")
    }

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        val response = exchange.response

        response.statusCode = HttpStatus.BAD_REQUEST
        response.headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

        if (ex is BaseException) {
            return ex.writeBody(response, buildErrorDto(messageHelper.getMessageByExchange(exchange, ex)))
        }
        if (ex is ConstraintViolationException) {
            return ex.writeBody(
                response,
                buildErrorDto(ex.message, status = 400, fields = ex.toError(exchange, messageHelper))
            )
        }
        if (ex is ValidationException) {
            return ex.writeBody(response, buildErrorDto(ex.message, status = 400))
        }
        if (ex is UnsupportedMediaTypeException) {
            response.statusCode = HttpStatus.UNSUPPORTED_MEDIA_TYPE
            return ex.writeBody(
                response,
                buildErrorDto(messageHelper.getMessageByExchange(exchange, INVALID_MEDIA_TYPE), INVALID_MEDIA_TYPE, 415)
            )
        }
        if (ex is ResponseStatusException) {
            response.statusCode = ex.statusCode
            return ex.writeBody(response, buildErrorDto(status = ex.statusCode.value()))
        }

        response.statusCode = HttpStatus.INTERNAL_SERVER_ERROR

        return ex.writeBody(response, buildErrorDto(), true)
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

    private fun Throwable.writeBody(response: ServerHttpResponse, message: Any, isError: Boolean = false): Mono<Void> =
        objectMapper.writeValueAsBytes(message)
            .let { Flux.just(response.bufferFactory().wrap(it)) }
            .let { response.writeWith(it) }
            .doFinally { logException(this, isError) }

    private fun logException(ex: Throwable, isError: Boolean) {
        if (isError || log.isDebugEnabled) {
            log.error("catch exception: ", ex)
        } else {
            log.debug("catch exception: {}", ex.message)
        }
    }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(ReactiveExceptionHandler::class.java)
    }
}