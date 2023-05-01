package io.lonmstalker.springkube.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.lonmstalker.springkube.exception.handler.ReactiveExceptionHandler
import io.lonmstalker.springkube.exception.handler.ServletExceptionHandler
import io.lonmstalker.springkube.helper.ClockHelper
import io.lonmstalker.springkube.helper.ReactiveMessageHelper
import io.lonmstalker.springkube.helper.ServletMessageHelper
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

@Configuration
@Import(ExceptionConfig.ReactiveErrorConfig::class, ExceptionConfig.ServletErrorConfig::class)
class ExceptionConfig {

    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    class ReactiveErrorConfig {

        @Bean
        @Order(Ordered.HIGHEST_PRECEDENCE)
        fun exceptionHandler(
            clockHelper: ClockHelper,
            objectMapper: ObjectMapper,
            messageHelper: ReactiveMessageHelper,
        ) = ReactiveExceptionHandler(clockHelper, objectMapper, messageHelper)
    }

    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    class ServletErrorConfig {

        @Bean
        @Order(Ordered.HIGHEST_PRECEDENCE)
        fun exceptionHandler(
            clockHelper: ClockHelper,
            objectMapper: ObjectMapper,
            servletMessageHelper: ServletMessageHelper
        ) = ServletExceptionHandler(clockHelper, objectMapper, servletMessageHelper)
    }
}