package com.example.router

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono

@Configuration
class CrudRouter {
    @Bean
    fun crudRoutes() =
        nest(path("/crud"),
            router {
                GET("") { ok().body(Mono.just("Select")) }
                POST("") { ok().body(Mono.just("Insert")) }
                PUT("") { ok().body(Mono.just("Update")) }
                DELETE("") { ok().body(Mono.just("Delete")) }
            }
        )
}
