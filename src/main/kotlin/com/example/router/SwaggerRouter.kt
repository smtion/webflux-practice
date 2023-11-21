package com.example.router

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono

@Configuration
class SwaggerRouter {
    @Bean
    fun swaggerSelectRoutes() = router { GET("crud") { ok().body(Mono.just("Select")) } }

    @Bean
    fun swaggerInsertRoutes() = router { POST("crud") { ok().body(Mono.just("Insert")) } }

    @Bean
    fun swaggerUpdateRoutes() = router { PUT("crud") { ok().body(Mono.just("Update")) } }

    @Bean
    fun swaggerDeleteRoutes() = router { DELETE("crud") { ok().body(Mono.just("Delete")) } }
}