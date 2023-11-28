package com.example.domain.enumtype

import com.example.common.enumtype.CodeEnum

enum class SaleStatusType(override val code: String) : CodeEnum {
    ACTIVE("A"),
    SOLDOUT("S"),
    INACTIVE("X"),
    ;
}