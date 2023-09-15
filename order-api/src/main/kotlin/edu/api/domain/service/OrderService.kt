package edu.api.domain.service

import edu.api.domain.entity.Order
import edu.api.infrastructure.supplier.SupplierApiGateway
import io.github.resilience4j.bulkhead.annotation.Bulkhead
import io.github.resilience4j.retry.annotation.Retry
import kotlinx.coroutines.*
import org.springframework.stereotype.Service
import java.time.Duration
import kotlin.concurrent.thread

@Service
class OrderService(
    private val supplierApiGateway: SupplierApiGateway
) {
    fun listOrders() = supplierApiGateway.listOrders()


    @Bulkhead(name = "bulkheadSupplierApi", fallbackMethod = "bulkHeadFallback")
    @Retry(name = "retrySupplierApi", fallbackMethod = "retryFallback")
    fun saveOrder(order: Order) {
        (1..100).forEach { i ->
            runBlocking(Dispatchers.Default) {
                launch (Dispatchers.IO){
                    println("${Thread.currentThread()} has run.")
                    supplierApiGateway.saveOrder(
                        order.copy(thread = i)
                    )
                }
            }
        }
    }

    fun bulkHeadFallback(t: Throwable) {
        println("Inside bulkHeadFallback, cause - $t")
        println("Inside bulkHeadFallback method. Some error occurred while calling service for seller registration")
        throw t
    }

    fun retryFallback(t: Throwable) {
        println("Inside retryFallback, cause - $t")
        throw t
    }
}