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
import reactor.util.function.Tuple2

@Service
class ApiService(
    private val dealProductRepository: DealProductRepository,
    private val productRepository: ProductRepository,
    private val employeeRepository: EmployeeRepository,
) {
    fun getProduct(productId: Long): Mono<Product> =
        productRepository.findById(productId)
            .switchIfEmpty { Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND)) }

    fun getEmployee(productId: Long): Mono<Employee> =
        getProduct(productId)
            .map { employeeRepository.findById(it.employeeId) }
            .flatMap { it }

    fun getProducts(dealId: Long): Flux<Product> =
        dealProductRepository.findByDealId(dealId)
            .map { it.productId }
            .collectList()
            .flatMapMany { productRepository.findAllById(it) }

    fun getProductsAndEmployee(dealId: Long): Flux<Pair<Product, Employee?>> =
        getProducts(dealId)
            .collectList()
            .flatMap { products: List<Product> ->
                val employeeIds: List<Long> = products.map(Product::employeeId).distinct()
                employeeRepository.findAllById(employeeIds)
                    .collectList()
                    .map { employees -> paringProductAndEmployee(products, employees) }
            }
            .flatMapIterable { it }

    private fun paringProductAndEmployee(products: List<Product>, employees: List<Employee>): List<Pair<Product, Employee?>>{
        val employeeMap: Map<Long, Employee> = employees.associateBy(Employee::employeeId)

        return products.map { product -> Pair(product, employeeMap[product.employeeId]) }
    }

    fun getProducts(page: PageRequest): Mono<PageImpl<Product>> =
        productRepository.findAllBy(page)
            .collectList()
            .zipWith(productRepository.countBy())
            .map { (list: List<Product>, count: Long) ->
                PageImpl(list, page, count)
            }

    private operator fun <T1, T2> Tuple2<T1, T2>.component1(): T1 {
        return this.t1
    }
    private operator fun <T1, T2> Tuple2<T1, T2>.component2(): T2 {
        return this.t2
    }
}


















