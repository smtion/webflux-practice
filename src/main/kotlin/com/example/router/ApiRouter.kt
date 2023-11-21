package com.example.router

import com.example.handler.ApiHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Configuration
class ApiRouter(
    private val apiHandler: ApiHandler
) {
    @Bean
    fun apiRoutes() =
        nest(path("api"),
            router {
                GET("") { ok().body("Let's implement WebFlux application!".toMono()) }
            }
        )
}