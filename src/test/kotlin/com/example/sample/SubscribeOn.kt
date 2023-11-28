package com.example.sample

import com.example.config.logger
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

class SubscribeOn {
    @Test
    @DisplayName("subscribeOn 사용 시 각 flow 의 구독과 처리를 어떤 스레드에서 하는지 살펴봅니다.")
    fun runSubscribeOn() {
        Flux.range(1, 3) // requests and runs on boundedElastic
            .log()
            .flatMap { Mono.just(it + 10) } // requests and runs on boundedElastic
            .log()
            .subscribeOn(Schedulers.parallel())
            .subscribe { logger.info { it } }

        Thread.sleep(100)
    }

    @Test
    @DisplayName("subscribeOn 위치가 다른 경우 각 flow 의 구독과 처리를 어떤 스레드에서 하는지 살펴봅니다.")
    fun runSubscribeOn2() {
        Flux.range(1, 3) // requests and runs on boundedElastic
            .log()
            .subscribeOn(Schedulers.parallel())
            .flatMap { Mono.just(it+10) } // requests on worker but runs on boundedElastic
            .log()
            .subscribe { logger.info { it } }
    }

    @Test
    @DisplayName("subscribeOn 을 여러 번 수행할 경우의 흐름을 살펴봅니다.")
    fun runMultipleSubscribeOn() {
        Flux.range(1, 3)
            .log()
            .subscribeOn(Schedulers.single())
            .flatMap { Mono.just(it + 10) }
            .log()
            .subscribeOn(Schedulers.parallel())
            .subscribe { logger.info { it } }

        Thread.sleep(100)
    }

    @Test
    @DisplayName("subscribeOn 과 publishOn 을 같이 사용한 경우의 흐름을 살펴봅니다.")
    fun runSubscribeOnWithPublishOn() {
        Flux.range(1, 3)
            .log()
            .flatMap { Mono.just(it + 10) }
            .log()
            .publishOn(Schedulers.single())
            .log()
            .flatMap { Mono.just(it + 100) }
            .log()
            .subscribeOn(Schedulers.parallel())
            .subscribe { logger.info { it } }

        Thread.sleep(100)
    }

    @Test
    @DisplayName("subscribeOn() 은 새로운 publishOn() 이전 까지만 유효합니다.")
    fun runWithNonBlockingApi() {
        Flux.range(1, 10)
            .log()
            .flatMap {
                val ms = (10L..1000L).random()
                logger.info { "Call ${ms}ms delayed API" }
                callApi(ms) // use reactor-http-nio
            }
            .log()
            .subscribeOn(Schedulers.boundedElastic())
            .subscribe { logger.info { it } }

        Thread.sleep(1100)
    }

    @Test
    @DisplayName("동기 코드를 비동기화 시킨 후 멀티 스레딩으로 처리합니다.")
    fun runWithBlockingApiOnFromCallable() {
        Flux.range(1, 10)
            .log()
            .flatMap {
                val ms = 300L
                logger.info { "Call ${ms}ms delayed API" }
                Mono.fromCallable { callBlockingApi(ms) }
                    .subscribeOn(Schedulers.boundedElastic())
            }
            .log()
            .subscribe { logger.info { it } }

        Thread.sleep(3100)
    }
}