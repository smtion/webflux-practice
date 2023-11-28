package com.example.repository.database.product

import com.example.domain.entity.Deal
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DealRepository : ReactiveCrudRepository<Deal, Long>