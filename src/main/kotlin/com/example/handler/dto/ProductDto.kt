package com.example.handler.dto

import com.example.domain.entity.Product

data class ProductDto(
    val productName: String,
) {
    constructor(product: Product): this(
        productName = product.productName
    )
}
