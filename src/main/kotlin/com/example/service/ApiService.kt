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
    fun getProduct(productId: Long): Mono<Product> =
        productRepository.findById(productId)
            .switchIfEmpty { Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND)) }

    fun getEmployee(productId: Long): Mono<Employee> =
        getProduct(productId)
            .flatMap {
                employeeRepository.findById(it.employeeId)
            }

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
                    .map { mds ->
                        val mdMap: Map<Long, Employee> = mds.associateBy(Employee::employeeId)

                        products.map { product ->
                            Pair(product, mdMap[product.employeeId])
                        }
                    }
            }
            .flatMapIterable { it }

    fun getProducts(page: PageRequest): Mono<PageImpl<Product>> =
        productRepository.findAllBy(page)
            .collectList()
            .zipWith(productRepository.countBy())
            .map { PageImpl(it.t1, page, it.t2 ) }
}
















