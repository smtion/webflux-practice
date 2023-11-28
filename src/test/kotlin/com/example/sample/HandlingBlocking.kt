package com.example.sample

import com.example.config.logger
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.toMono

class HandlingBlocking {
    @Test
    @DisplayName("동기 코드는 flatMap 에서 비동기적으로 동작하지 않습는다. 동기 코드를 다른 스레드에서 동작시키는 것에 불과합니다.")
    fun runWithBlockingApi() {
        Flux.range(1, 10)
            .log()
            .flatMap {
                val ms = 300L
                logger.info { "Call ${ms}ms delayed API" }
                callBlockingApi(ms).toMono().publishOn(Schedulers.boundedElastic()) // doesn't work as you intended
            }
            .log()
            .subscribe { logger.info { it } }

        Thread.sleep(3100)
    }

    @Test
    @DisplayName("동기 코드를 동시에 처리하려면 병렬처리를 해야합니다.")
    fun runWithBlockingApiOnParallel() {
        Flux.range(1, 10)
            .log()
            .parallel()
            .runOn(Schedulers.parallel())
            .flatMap {
                val ms = 300L
                logger.info { "Call ${ms}ms delayed API" }
                callBlockingApi(ms).toMono()
            }
            .log()
            .subscribe { logger.info { it } }

        Thread.sleep(400)
    }

    @Test
    @DisplayName("동기 코드를 비동기화 시킨 후 멀티 스레딩으로 처리합니다.")
    fun runWithBlockingApiOnFromCallable() {
        Flux.range(1, 10)
            .log()
            .flatMap {
                val ms = 300L //(300L..500L).random()
                logger.info { "Call ${ms}ms delayed API" }
                Mono.fromCallable { callBlockingApi(ms) }
                    .publishOn(Schedulers.boundedElastic())
            }
            .log()
            .subscribe { logger.info { it } }

        Thread.sleep(2000)
    }

    @Test
    @DisplayName("runWithBlockingApiOnFromCallable() 를 실전 코드로 작성해 봅니다.")
    fun runWithBlockingApiOnFromCallable2() {
        Flux.range(1, 10)
            .log()
            .flatMap {
                val ms = 300L //(300L..500L).random()
                logger.info { "Call ${ms}ms delayed API" }
                callApi2(ms)
            }
            .log()
            .subscribe { logger.info { it } }

        Thread.sleep(3100)
    }
}