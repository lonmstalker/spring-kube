package io.lonmstalker.springkube.config

import io.lonmstalker.springkube.filter.ReactiveRequestContextWebFilter
import io.lonmstalker.springkube.filter.ServletRequestContextFilter
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.server.i18n.LocaleContextResolver
import org.springframework.web.servlet.LocaleResolver

@Configuration
@Import(ContextConfig.ReactiveContextConfig::class, ContextConfig.ServletContextConfig::class)
class ContextConfig {


    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    class ReactiveContextConfig {
        @Bean
        fun requestContextWebFilter(localeContextResolver: LocaleContextResolver) =
            ReactiveRequestContextWebFilter(localeContextResolver)
    }

    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    class ServletContextConfig {
        @Bean
        fun requestContextWebFilter(localeResolver: LocaleResolver) = ServletRequestContextFilter(localeResolver)
    }
}