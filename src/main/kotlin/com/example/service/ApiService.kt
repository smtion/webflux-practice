package com.example.service

import com.example.domain.entity.Employee
import com.example.domain.entity.Product
import com.example.repository.database.product.DealProductRepository
import com.example.repository.database.product.EmployeeRepository
import com.example.repository.database.product.ProductRepository
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class ApiService(
    private val dealProductRepository: DealProductRepository,
    private val productRepository: ProductRepository,
    private val employeeRepository: EmployeeRepository,
) {
    // Todo Practice 1
//    fun getProduct()

    // Todo Practice 2
//    fun getEmployee()

    // Todo Practice 3
//    fun getProducts()

    // Todo Practice 4
//    fun getProductsAndEmployee(dealId: Long): Flux<Pair<Product, Employee?>>

    // Todo Practice 5
//    fun getProducts(page: PageRequest): Mono<PageImpl<Product>>
}
















