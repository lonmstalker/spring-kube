package io.lonmstalker.springkube.config

import org.jetbrains.exposed.spring.SpringTransactionManager
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class ExposedConfig {

    @Bean
    fun databaseCfg() = DatabaseConfig { this.useNestedTransactions = true }

    @Bean
    fun database(dataSource: DataSource) = Database.connect(dataSource)

    @Bean
    fun transactionManager(dataSource: DataSource) = SpringTransactionManager(dataSource)
}