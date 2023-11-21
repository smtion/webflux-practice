package com.example.handler

import com.example.handler.dto.ProductDto
import com.example.handler.dto.ProductEmployeeDto
import com.example.service.ApiService
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

@Component
class ApiHandler(
    private val apiService: ApiService
) {
    fun getProduct(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just("implement here"))

    fun getEmployee(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just("implement here"))

    fun getProducts(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just("implement here"))

    fun getProductsAndEmployee(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just("implement here"))

    fun getPagingProducts(req: ServerRequest): Mono<ServerResponse> {
        return ok().body(Mono.just("implement here"))
    }
}