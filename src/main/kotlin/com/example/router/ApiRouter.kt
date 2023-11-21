package com.example.router

import com.example.handler.ApiHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.router

@Configuration
class ApiRouter(
    private val apiHandler: ApiHandler
) {
    @Bean
    fun apiRoutes() =
        nest(path("api"),
            router {
                GET("products/{productId}", apiHandler::getProduct)
                GET("products/{productId}/employee", apiHandler::getEmployee)
                GET("deals/{dealId}/products", apiHandler::getProducts)
                GET("deals/{dealId}/products:employee", apiHandler::getProductsAndEmployee)
                GET("products", apiHandler::getPagingProducts)
            }
        )
}