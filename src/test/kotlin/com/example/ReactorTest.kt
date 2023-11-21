package com.example

import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.time.Duration

private val logger = KotlinLogging.logger {}

class ReactorTest {

    @Test
    fun parallel() {
        Flux.range(1, 10)
            .parallel(10)
            .runOn(Schedulers.boundedElastic())
            .flatMap {
                val ms = (10L..1000L).random()
                logger.info { "ms: $ms" }
                Mono.just(ms).delayElement(Duration.ofMillis(ms))
            }
            .sequential()
            .collectList()
            .subscribe { logger.info { "List: $it" }  }

        Thread.sleep(1100)
    }
}