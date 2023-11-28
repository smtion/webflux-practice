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
        ok().body(
            apiService.getProduct(req.pathVariable("productId").toLong())
                .map { ProductDto(it) }
        )

    fun getEmployee(req: ServerRequest): Mono<ServerResponse> =
        ok().body(
            apiService.getEmployee(req.pathVariable("productId").toLong())
        )

    fun getProducts(req: ServerRequest): Mono<ServerResponse> =
        ok().body(
            apiService.getProducts(req.pathVariable("dealId").toLong())
                .map { ProductDto(it) }
        )

    fun getProductsAndEmployee(req: ServerRequest): Mono<ServerResponse> =
        ok().body(
            apiService.getProductsAndEmployee(req.pathVariable("dealId").toLong())
                .map { ProductEmployeeDto(it) }
        )

    fun getPagingProducts(req: ServerRequest): Mono<ServerResponse> {
        val page: Int = req.queryParam("page").orElse("0").toInt()
        val size: Int = req.queryParam("size").orElse("10").toInt()
        val pageable = PageRequest.of(page, size)

        return ok().body(
            apiService.getProducts(pageable)
        )
    }
}