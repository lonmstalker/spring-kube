package io.lonmstalker.springkube.config

import io.lonmstalker.springkube.annotation.EnableReactiveJooq
import io.lonmstalker.springkube.model.Public.Companion.PUBLIC
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableReactiveJooq
class JooqConfig {

    @Bean
    fun publicSchema() = PUBLIC
}