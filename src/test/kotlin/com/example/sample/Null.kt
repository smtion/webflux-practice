package com.example.sample

import com.example.config.logger
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.switchIfEmptyDeferred
import reactor.kotlin.core.publisher.toMono
import java.util.*
import kotlin.jvm.optionals.getOrNull

class Null {
    @Test
    @DisplayName("리액터 체인에서 null value 를 반환하면 NPE 가 발생합니다.")
    fun returnNull() {
        Flux.range(1, 10)
            .map {
                if (it % 2 == 0) null
                else it
            }
            .collectList()
            .subscribe { logger.info { it } }
    }

    @Test
    @DisplayName("Flux 에서 일부만 empty 를 반환하는 케이스의 흐름을 살펴봅니다.")
    fun returnEmptyOnFlux() {
        Flux.range(1, 10)
            .flatMap {
                if (it % 2 == 0) Mono.empty()
                else it.toMono()
            }
            .log()
            .flatMap { Mono.just(it * 100) }
            .log()
            .subscribe { logger.info { it } }
    }

    @Test
    @DisplayName("Flux 에서 모두 empty 를 반환하는 케이스의 흐름를 살펴봅니다.")
    fun returnEmptyOnFlux2() {
        Flux.range(1, 10)
            .flatMap {
                if (it > 0) Mono.empty()
                else it.toMono()
            }
            .log()
            .flatMap { Mono.just(it * 100) }
            .log()
            .collectList()
            .subscribe { logger.info { it } }
    }

    @Test
    @DisplayName("Mono 에서 empty 를 반환하는 케이스의 흐름을 살펴봅니다.")
    fun returnEmptyOnMono() {
        Mono.just(1)
            .flatMap {
                if (it == 1) Mono.empty()
                else it.toMono()
            }
            .log()
            .flatMap { Mono.just(it * 100) }
            .log()
            .subscribe { logger.info { it } }
    }

    @Test
    @DisplayName("Mono empty 를 처리하는 방법을 알아봅니다.")
    fun handleEmptyOnMono() {
        callApiEmptyMono()
//            .switchIfEmpty { Mono.just(0) } // is it correct to return 0 by default?
//            .switchIfEmpty { Mono.just(Optional.empty<Int>()) } // cannot wrap with Optional
            .map {
                logger.info { "Does not reach here" }
                Optional.of(it)
            } // (1-1) return Optional
            .switchIfEmpty {
                logger.info { "empty" }
                Mono.just(Optional.empty())
            } // (1-2) return Optional
            .subscribe { logger.info { it.getOrNull() } }
    }

    @Test
    @DisplayName("Flux empty 를 처리하는 방법을 알아봅니다.")
    fun handleEmptyOnFlux() {
        callApiEmptyFlux()
            .map {
                logger.info { "Does not reach here" }
                Optional.of(it)
            }
            .switchIfEmptyDeferred {
                logger.info { "empty" }
                Flux.just(Optional.empty())
            }
            .subscribe { logger.info { it.getOrNull() } }
    }

    @Test
    @DisplayName("Optional 을 사용하여 null value 를 처리합니다.")
    fun returnOptionalNullable() {
        Mono.just(1)
            .map {
                if (it > 0) Optional.ofNullable(null)
                else Optional.of(it)
            }
            .flatMap {
                if (it.isPresent) callApiIfNull()
                else callApiIfNotNull()
            }
            .subscribe { logger.info { it } }
    }

    @Test
    @DisplayName("Optional.empty() 를 null value 대용으로 사용합니다.")
    fun returnOptionalEmpty() {
        Mono.just(1)
            .map {
                if (it > 0) Optional.empty() // instead of ofNullable()
                else Optional.of(it)
            }
            .flatMap {
                if (it.isPresent) callApiIfNull()
                else callApiIfNotNull()
            }
            .subscribe { logger.info { it } }
    }
}