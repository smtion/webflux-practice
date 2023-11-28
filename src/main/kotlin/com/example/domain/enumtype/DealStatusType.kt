package com.example.domain.enumtype

import com.example.common.enumtype.CodeEnum

enum class DealStatusType(override val code: String) : CodeEnum {
    ACTIVE("A"),
    INACTIVE("I"),
    ;
}