package io.lonmstalker.springkube.config

import io.r2dbc.spi.ConnectionFactory
import org.jooq.impl.DSL
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JooqConfig {

    @Bean
    fun ctx(connectionFactory: ConnectionFactory) = DSL.using(connectionFactory)
}