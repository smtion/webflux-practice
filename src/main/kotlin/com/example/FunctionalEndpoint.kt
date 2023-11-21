package com.example

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono

@Component
class Handler {
    fun say(req: ServerRequest): Mono<ServerResponse> =
        ServerResponse.ok().body(Mono.just("Hello world"))
}

@Configuration
class Router(
    private val handler: Handler
) {
    @Bean
    fun sampleRoutes() =
        router {
            GET("/functional", handler::say)
        }
}