package com.example.sample

import com.example.config.logger
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

class Defer {
    @Test
    fun eagerEvaluation() {
        Mono.just(1)
            .switchIfEmpty(getDefaultMono())
            .subscribe()

        Thread.sleep(100)
    }

    @Test
    fun lazyEvaluation() {
        Mono.just(1)
            .switchIfEmpty(Mono.defer { getDefaultMono() })
            .subscribe()

        Thread.sleep(100)
    }

    @Test
    fun lazyEvaluation2() {
        Mono.just(1)
            .switchIfEmpty(Mono.fromSupplier { getDefault() })
            .subscribe()

        Thread.sleep(100)
    }

    @Test
    fun lazyEvaluation3() {
        Mono.just(1)
            .switchIfEmpty { getDefaultMono() }
            .subscribe()

        Thread.sleep(100)
    }

    private fun getDefaultMono(): Mono<Int> {
        logger.info { "empty" }

        return Mono.just(2)
    }

    private fun getDefault(): Int {
        logger.info { "empty" }

        return 2
    }
}