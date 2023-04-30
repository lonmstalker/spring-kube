package io.lonmstalker.springkube

import io.lonmstalker.springkube.utils.ApplicationUtils
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.core.publisher.Hooks

@SpringBootApplication
class SpringKubeApplication

fun main(args: Array<String>) {
    ApplicationUtils.init()
    runApplication<SpringKubeApplication>(*args)
}
