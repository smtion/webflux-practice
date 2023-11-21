package com.example.domain.entity

import org.springframework.data.relational.core.mapping.Table

@Table
data class DealProduct(
    val dealId: Long,
    val productId: Long,
)
