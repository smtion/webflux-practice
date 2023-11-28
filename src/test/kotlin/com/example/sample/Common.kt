package com.example.sample

import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.lang.RuntimeException

fun callApi(ms: Long): Mono<Long> =
    WebClient.create("http://localhost:8882").get()
        .uri("/delay/$ms")
        .retrieve()
        .bodyToMono()

fun callBlockingApi(ms: Long): Long =
    WebClient.create("http://localhost:8882").get()
        .uri("/delay/$ms")
        .retrieve()
        .bodyToMono<Long>()
        .block()!!

fun callApi2(ms: Long): Mono<Long> =
    Mono.fromCallable { callBlockingApi(ms) }
        .publishOn(Schedulers.boundedElastic())

fun callError(): Mono<Long> = Mono.error(IllegalStateException("error"))

fun callApiEmptyMono(): Mono<Int> = Mono.empty()

fun callApiEmptyFlux(): Flux<Int> = Flux.empty()

fun callApiIfNull(): Mono<Int> = Mono.just(1)

fun callApiIfNotNull(): Mono<Int> = Mono.just(2)