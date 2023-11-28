package com.example.repository.database.product

import com.example.domain.entity.DealProduct
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface DealProductRepository : ReactiveCrudRepository<DealProduct, Long> {
    fun findByDealId(dealId: Long): Flux<DealProduct>
}