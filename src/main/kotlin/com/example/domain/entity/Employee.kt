package com.example.domain.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
data class Employee(
    @Id
    val employeeId: Long,
    val employeeName: String,
)
