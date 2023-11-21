package com.example.handler.dto

import com.example.domain.entity.Employee
import com.example.domain.entity.Product

data class ProductEmployeeDto(
    val productName: String,
    val employeeName: String?,
) {
    constructor(pair: Pair<Product, Employee?>): this(
        pair.first.productName,
        pair.second?.employeeName
    )
}
