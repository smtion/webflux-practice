package com.example.domain.entity

import com.example.domain.enumtype.SaleStatusType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
data class Product(
    @Id
    val productId: Long,
    val productName: String,
    val employeeId: Long,
    val saleStatus: SaleStatusType,
)
