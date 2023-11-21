package com.example.repository.database.product

import com.example.domain.entity.Employee
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository : ReactiveCrudRepository<Employee, Long>