package com.example.config.kms

import org.springframework.core.io.ClassPathResource
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets
import java.time.Duration

fun <T> loadKmsFile(mapper: (jsonString: String) -> T): Mono<T> =
    Mono.just(ClassPathResource("kms.json").getContentAsString(StandardCharsets.UTF_8))
        .delayElement(Duration.ofMillis(100))
        .onErrorResume {
            // describe fallback here
            Mono.error(it)
        }
        .map {
            mapper(it)
        }