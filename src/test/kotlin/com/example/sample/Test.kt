package com.example.sample

import com.example.config.logger
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class Test {
    @Test
    fun expectNextCount() {
        StepVerifier.create(Flux.just(1, 2, 3))
            .expectNextCount(3)
            .verifyComplete()
    }

    @Test
    fun expectNextCount2() {
        StepVerifier.create(Flux.just(1, 2, 3))
            .expectNextCount(2)
            .assertNext { it == 3 }
            .verifyComplete()
    }

    @Test
    fun thenConsumeWhile() {
        StepVerifier.create(Flux.just(1, 2, 3))
            .thenConsumeWhile { it < 2 }
            .expectNext(2)
            .expectNext(3)
            .verifyComplete()
    }

    @Test
    fun thenConsumeWhile2() {
        StepVerifier.create(Flux.just(1, 2, 3))
            .thenConsumeWhile { it != null }
            .verifyComplete()
    }

    @Test
    fun then() {
        StepVerifier.create(Flux.just(1, 2, 3))
            .expectNext(1)
            .then { logger.info { "1" } }
            .expectNextCount(2)
            .verifyComplete()
    }

    @Test
    fun expectError() {
        StepVerifier.create(Mono.error<Int>(RuntimeException()))
            .expectError(RuntimeException::class.java)
            .verify()
    }

    @Test
    fun expectErrorMessage() {
        StepVerifier.create(Mono.error<Int>(RuntimeException("error")))
            .expectErrorMessage("error")
            .verify()
    }

    @Test
    fun expectErrorMatches() {
        StepVerifier.create(Mono.error<Int>(RuntimeException("runtime exception")))
            .expectErrorMatches {
//                it is RuntimeException
                it.message?.startsWith("runtime") == true
            }
            .verify()
    }
}