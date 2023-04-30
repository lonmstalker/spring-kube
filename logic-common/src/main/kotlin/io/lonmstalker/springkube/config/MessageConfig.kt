package io.lonmstalker.springkube.config

import io.lonmstalker.springkube.helper.ReactiveMessageHelper
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.server.i18n.AcceptHeaderLocaleContextResolver
import org.springframework.web.server.i18n.LocaleContextResolver
import java.util.*

@Configuration
@Import(MessageConfig.ReactiveMessageConfig::class)
class MessageConfig {

    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    class ReactiveMessageConfig {

        @Bean
        fun localeResolver(): LocaleContextResolver =
            AcceptHeaderLocaleContextResolver()
                .apply { this.defaultLocale = Locale.forLanguageTag("ru") }

        @Bean
        fun messageHelper(messageSource: MessageSource, localeContextResolver: LocaleContextResolver) =
            ReactiveMessageHelper(messageSource, localeContextResolver)

    }
}