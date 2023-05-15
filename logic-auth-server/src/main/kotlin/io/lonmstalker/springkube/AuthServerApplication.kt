package io.lonmstalker.springkube

import io.lonmstalker.springkube.config.properties.AppProperties
import io.lonmstalker.springkube.config.properties.TokenClients
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(AppProperties::class, TokenClients::class)
class AuthServerApplication

fun main(args: Array<String>) {
    runApplication<AuthServerApplication>(*args)
}
