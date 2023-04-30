package io.lonmstalker.springkube.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.lonmstalker.springkube.exception.handler.ReactiveExceptionHandler
import io.lonmstalker.springkube.helper.ClockHelper
import io.lonmstalker.springkube.helper.ReactiveMessageHelper
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(ExceptionConfig.ReactiveErrorConfig::class)
class ExceptionConfig {

    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    class ReactiveErrorConfig {

        @Bean
        fun exceptionHandler(
            clockHelper: ClockHelper,
            messageHelper: ReactiveMessageHelper,
            objectMapper: ObjectMapper
        ) = ReactiveExceptionHandler(clockHelper, objectMapper, messageHelper)
    }
}