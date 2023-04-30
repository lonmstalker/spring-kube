package io.lonmstalker.springkube.config

import io.lonmstalker.springkube.filter.RequestContextWebFilter
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.server.i18n.LocaleContextResolver

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
class ContextConfig {

    @Bean
    fun requestContextWebFilter(localeContextResolver: LocaleContextResolver) =
        RequestContextWebFilter(localeContextResolver)
}