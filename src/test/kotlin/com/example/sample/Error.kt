package com.example.sample

import com.example.config.logger
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.retry.Retry
import java.time.Duration
import java.util.function.Predicate


class Error {
    @Test
    @DisplayName("Reactor 에서 try catch 는 도움이 되지 않습니다.")
    fun errorWithTryCatch() {
        Mono.just(1)
            .flatMap {
                try {
                    callError()
                } catch (e: Exception) {
                    logger.info { "Exception caught" }
                    Mono.just(0)
                }
            }
            .subscribe { logger.info { it } }
    }

    @Test
    @DisplayName("onErrorReturn 은 stream 을 종료합니다.")
    fun errorReturn() {
        threeFive()
            .onErrorReturn(0)
            .map { it + 100 }
            .subscribe(
                { logger.info { it } },
                { logger.error { "error" } },
                { logger.info { "completed" } },
            )
    }

    @Test
    fun errorReturn2() {
        threeFive()
            .map { it + 100 }
            .onErrorReturn(ThreeException::class.java, 0)
            .onErrorReturn(FiveException::class.java, -1)
            .subscribe { logger.info { it } }
    }

    @Test
    fun errorReturn3() {
        threeFive()
            .map { it + 100 }
            .onErrorReturn({ e -> e is ThreeException || e is FiveException }, 0)
            .subscribe { logger.info { it } }
    }

    @Test
    @DisplayName("onErrorReturn 은 stream 을 종료합니다")
    fun errorResume() {
        threeFive()
            .onErrorResume { Mono.just(0) }
            .map { it + 100 }
            .subscribe(
                { logger.info { it } },
                { logger.error { "error" } },
                { logger.info { "completed" } },
            )
    }

    @Test
    fun errorResume2() {
        threeFive()
            .map { it + 100 }
            .onErrorResume(ThreeException::class.java) { Mono.just(0) }
            .onErrorResume(FiveException::class.java) { Mono.just(-1) }
            .subscribe { logger.info { it } }
    }

    @Test
    fun errorResume3() {
        threeFive()
            .map { it + 100 }
            .onErrorResume({ e -> e is ThreeException || e is FiveException }) { Mono.just(0) }
            .subscribe { logger.info { it } }
    }

    @Test
    @DisplayName("다른 예외로 전파")
    fun errorResume4() {
        threeFive()
            .map { it + 100 }
            .onErrorResume {
//                Mono.just(RuntimeException()) // T must be Int
                throw RuntimeException()
            }
            .subscribe { logger.info { it } }
    }

    @Test
    @DisplayName("onErrorReturn 은 stream 을 종료하지 않습니다.")
    fun errorContinue() {
        threeFive()
            .map { it + 100 }
            .onErrorContinue { _, v ->
                logger.warn { "value $v is not allowed" }
            }
            .subscribe(
                { logger.info { it } },
                { logger.error { "error" } },
                { logger.info { "completed" } },
            )
    }

    @Test
    @DisplayName("Upstream 에 대한 에러 처리는 하나의 onErrorContinue 가 담당한다.")
    fun errorContinue2() {
        threeFive()
            .map { it + 100 }
            .onErrorContinue(ThreeException::class.java) { _, _ ->
                logger.warn { "You should shout Jjak!" }
            } // upstream 에 대한 모든 에러 핸들링 책임을 가진다.
            .onErrorContinue(FiveException::class.java) { _, _ ->
                logger.warn { "You should shout Bbang!" }
            } // 위에 있는 onErrorContinue 가 모든 책임을 가져가서 동작하지 않는다.
            .subscribe { logger.info { it } }
    }

    @Test
    @DisplayName("Upstream 에 대한 에러 처리 책임이 분리되었다.")
    fun errorContinue3() {
        Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .flatMap {
                if (it % 3 == 0) Mono.error(ThreeException("Jjak!"))
                else Mono.just(it)
            }
            .onErrorContinue(ThreeException::class.java) { _, _ ->
                logger.warn { "You should shout Jjak!" }
            }
            .flatMap {
                if (it % 5 == 0) Mono.error(FiveException("Bbang!"))
                else Mono.just(it)
            }
            .onErrorContinue(FiveException::class.java) { _, _ ->
                logger.warn { "You should shout Bbang!" }
            }
            .subscribe { logger.info { it } }
    }

    @Test
    fun errorContinue4() {
        threeFive()
            .map { it + 100 }
            .onErrorContinue(Predicate { e -> e is ThreeException || e is FiveException}) { _, _ ->
                logger.warn { "You should shout Jjak or Bbang!" }
            }
            .subscribe { logger.info { it } }
    }

    @Test
    @DisplayName("베스트 솔루션")
    fun errorContinue5() {
        threeFive()
            .map { it + 100 }
            .onErrorContinue { e, _ ->
                when (e) {
                    is ThreeException -> logger.warn { "You should shout Jjak!" }
                    is FiveException -> logger.warn { "You should shout Bbang!" }
                    else -> logger.warn { "You should shout...just run away" }
                }
            }
    }

    @Test
    @DisplayName("다른 예외로 전파")
    fun errorContinue6() {
        threeFive()
            .map { it + 100 }
            .onErrorContinue { _, _ ->
                throw RuntimeException()
            }
            .subscribe { logger.info { it } }
    }

    @Test
    @DisplayName("onErrorReturn 와 유사하지만 fallback value 를 가지지 않습니다.")
    fun errorComplete() {
        threeFive()
            .onErrorComplete()
            .map { it + 100 }
            .subscribe(
                { logger.info { it } },
                { logger.error { "error" } },
                { logger.info { "completed" } },
            )
    }

    @Test
    @DisplayName("onErrorReturn 와 유사하지만 fallback value 를 가지지 않습니다.")
    fun errorStop() {
        threeFive()
            .onErrorStop()
            .map { it + 100 }
            .subscribe(
                { logger.info { it } },
                { logger.error { "error" } },
                { logger.info { "completed" } },
            )
    }

    @Test
    fun retry() {
        threeFive()
            .retry(2)
            .map { it + 100 }
            .subscribe(
                { logger.info { it } },
                { logger.error { "error" } },
                { logger.info { "completed" } },
            )
    }

    @Test
    fun retryWhen() {
        threeFive()
            .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
            .map { it + 100 }
            .subscribe(
                { logger.info { it } },
                { logger.error { "error" } },
                { logger.info { "completed" } },
            )

        Thread.sleep(1000)
    }

    @Test
    fun retryWhen2() {
        threeFive()
            .retryWhen(Retry.max(3))
            .map { it + 100 }
            .subscribe(
                { logger.info { it } },
                { logger.error { "error" } },
                { logger.info { "completed" } },
            )

        Thread.sleep(1000)
    }

    @Test
    fun gpt() {
        val numbers = Flux.just("1", "two", "trj", "four", "5")

        numbers
            .map { str -> str.toInt() }
            .retryWhen(Retry.maxInARow(3)) // 최대 2번의 재시도
            .subscribe(
                { value -> logger.info { "Processed value: $value" } },
                { error -> logger.info { "Unhandled error: ${error.message}" } }
            )
    }

    private fun threeFive(): Flux<Int> =
        Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .flatMap {
                if (it % 3 == 0) Mono.error(ThreeException("Jjak!"))
                else if (it % 5 == 0) Mono.error(FiveException("Bbang!"))
                else Mono.just(it)
            }

    class ThreeException(msg: String) : Exception(msg)

    class FiveException(msg: String) : Exception(msg)
}

class GlobalErrorAttributes : DefaultErrorAttributes() {
    override fun getErrorAttributes(request: ServerRequest, options: ErrorAttributeOptions): Map<String, Any> {
        val map: MutableMap<String, Any> = super.getErrorAttributes(
            request, options
        )
        map["status"] = HttpStatus.BAD_REQUEST
        map["message"] = "something is wrong"
        return map
    }
}

