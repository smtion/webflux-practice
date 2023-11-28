package com.example.repository.database.product

import com.example.domain.entity.Product
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface ProductRepository : ReactiveCrudRepository<Product, Long> {
    fun findAllBy(page: Pageable): Flux<Product>

    fun countBy(): Mono<Long>
}