package io.lonmstalker.springkube

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringKubeApplication

fun main(args: Array<String>) {
    System.setProperty("reactor.netty.http.server.accessLogEnabled", "true")
    runApplication<SpringKubeApplication>(*args)
}
