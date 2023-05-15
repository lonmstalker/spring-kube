package io.lonmstalker.springkube.config

import io.lonmstalker.springkube.helper.JooqHelper
import io.r2dbc.spi.ConnectionFactory
import org.jooq.impl.DSL
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(JooqHelper::class)
@ConditionalOnClass(name = ["io.r2dbc.spi.ConnectionFactory"])
class ReactiveJooqConfig {

    @Bean
    fun ctx(connectionFactory: ConnectionFactory) = DSL.using(connectionFactory)
}