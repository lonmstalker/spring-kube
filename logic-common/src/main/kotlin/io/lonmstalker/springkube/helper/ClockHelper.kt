package io.lonmstalker.springkube.helper

import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime
import java.time.OffsetDateTime

@Component
class ClockHelper(private val clock: Clock) {

    fun clock(): LocalDateTime = LocalDateTime.now(this.clock)

    fun clockOffset(): OffsetDateTime = OffsetDateTime.now(this.clock)
}