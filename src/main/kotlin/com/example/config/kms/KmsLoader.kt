package com.example.config.kms

import org.springframework.core.io.ClassPathResource
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets

fun <T> loadKmsFile(mapper: (jsonString: String) -> T): Mono<T> =
    Mono.just(ClassPathResource("kms.json").getContentAsString(StandardCharsets.UTF_8))
        .onErrorResume {
            // describe fallback here
            Mono.error(it)
        }
        .map {
            mapper(it)
        }