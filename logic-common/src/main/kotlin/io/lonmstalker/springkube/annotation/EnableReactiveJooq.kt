package io.lonmstalker.springkube.annotation

import io.lonmstalker.springkube.config.ReactiveJooqConfig
import org.springframework.context.annotation.Import

@Import(ReactiveJooqConfig::class)
annotation class EnableReactiveJooq
