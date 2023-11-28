package com.example.sample

import com.example.config.logger
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

class Parallel {
    @Test
    fun parallel() {
        Flux.range(1, 10)
            .parallel(3)
            .runOn(Schedulers.boundedElastic())
            .flatMap {
                val ms = (10L..1000L).random()
                logger.info { "ms: $ms" }
                Thread.sleep(ms)
                Mono.just(ms)
            }
            .sequential()
            .collectList()
            .subscribe { logger.info { "List: $it" }  }

        Thread.sleep(100)
    }

    @Test
    fun a() {
        Flux.range(1, 100)
            .log()
            .flatMap({ Mono.just(it * 100)}, 6)
            .log()
            .publishOn(Schedulers.single())
            .subscribe()
    }
}