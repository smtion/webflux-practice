package com.example.sample

import com.example.config.logger
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.toMono
import java.time.Duration

class PublishOn {
    @Test
    @DisplayName("publishOn 사용 시 각 flow 의 처리를 어떤 스레드에서 하는지 살펴봅니다.")
    fun runPublishOn() {
        Flux.just(1, 2, 3)
            .log()
            .publishOn(Schedulers.parallel())
            .log()
            .flatMap {
                logger.info { "I am parallel thread" }
                val mono = Mono.just(it + 10)

                if (it == 2)
                    mono.delayElement(Duration.ofMillis(100)) // non sequential process
                else
                    mono
            }
            .log()
            .flatMap { Mono.just(it + 100) }
            .log()
            .publishOn(Schedulers.boundedElastic())
            .log()
            .subscribe { logger.info { it } }

        Thread.sleep(300)
    }

    @Test
    @DisplayName("WebClient 사용 시 별도의 reactor-http-nio 스레드 풀을 사용합니다.")
    fun runWithNonBlockingApi() {
        Flux.range(1, 10)
            .log()
            .flatMap {
                val ms = (10L..1000L).random()
                logger.info { "Call ${ms}ms delayed API" }
                callApi(ms) // use reactor-http-nio
            }
            .log()
            .subscribe { logger.info { it } }

        Thread.sleep(1100)
    }

    @Test
    fun runWithNonBlockingApiOnOtherScheduler() {
        Flux.range(1, 10)
            .log()
            .flatMap {
                val ms = (10L..1000L).random()
                logger.info { "Call ${ms}ms delayed API" }
                callApi(ms) // use reactor-http-nio
                    .log()
            }
            .publishOn(Schedulers.boundedElastic()) // change scheduler
            .log()
            .subscribe { logger.info { it } }

        Thread.sleep(1100)
    }

    @Test
    @DisplayName("스케줄러를 지정하지 않아도 parallel 스케줄러를 사용합니다. 아마도 assembly time 에 적절한 스케줄러를 선택하는 매커니즘이 있나 봅니다.")
    fun runPublishOn2() {
        Flux.range(1, 1000)
            .flatMap {
                val ms = (10L..100L).random()
                Mono.just(it + 100_000).delayElement(Duration.ofMillis(ms))
            }
            .log()
            .collectList()
            .subscribe { logger.info { it } }

        Thread.sleep(300)
    }
}