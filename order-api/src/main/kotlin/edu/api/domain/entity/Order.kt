package edu.api.domain.entity

import java.time.LocalDateTime
import java.util.*

data class Order (
    val id: String = UUID.randomUUID().toString(),
    val product: String,
    val quantity: Int,
    val thread: Int = 0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val insertedAt: LocalDateTime? = null
)
