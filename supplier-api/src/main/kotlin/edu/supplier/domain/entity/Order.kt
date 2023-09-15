package edu.supplier.domain.entity

import java.time.LocalDateTime
import java.util.*

data class Order (
    val id: String = UUID.randomUUID().toString(),
    val product: String,
    val quantity: Int,
    val thread: Int,
    val createdAt: LocalDateTime,
    val insertedAt: LocalDateTime? = LocalDateTime.now()
)
