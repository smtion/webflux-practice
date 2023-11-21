package com.example.domain.entity

import com.example.domain.enumtype.DealStatusType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
data class Deal(
    @Id
    val dealId: Long,
    val dealName: String,
    val dealStatus: DealStatusType,
)
