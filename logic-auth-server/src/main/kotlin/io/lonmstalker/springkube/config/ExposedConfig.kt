package io.lonmstalker.springkube.config

import org.jetbrains.exposed.sql.DatabaseConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ExposedConfig {

    @Bean
    fun databaseCfg() = DatabaseConfig { this.useNestedTransactions = true }
}