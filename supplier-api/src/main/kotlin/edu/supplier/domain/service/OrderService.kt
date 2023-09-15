package edu.supplier.domain.service

import edu.supplier.application.web.OrderController
import edu.supplier.domain.entity.Order
import io.github.resilience4j.bulkhead.annotation.Bulkhead
import io.github.resilience4j.ratelimiter.RequestNotPermitted
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import io.github.resilience4j.retry.annotation.Retry
import org.apache.tomcat.jni.Local
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime

@Service
class OrderService {

    private val logger = LoggerFactory.getLogger(OrderService::class.java)

    private var listOfOrders: MutableList<Order> = mutableListOf(
        Order(
            product = "Papel",
            quantity = 2,
            thread = 0,
            createdAt = LocalDateTime.now(),
            insertedAt = LocalDateTime.now()
        )
    )

    @Retry(name = "save-order", fallbackMethod = "saveOrderRetryFallBack")
    @RateLimiter(name = "save-order", fallbackMethod = "saveOrderFallBack")
    @Bulkhead(name = "bulkheadSupplierApi", fallbackMethod = "bulkHeadFallback")
    fun save(order: Order) {
        Thread.sleep(500)
        logger.info("Try execute")
        listOfOrders.add(order.copy(insertedAt = LocalDateTime.now()))
    }

    fun bulkHeadFallback(t: Throwable) {
        println("Inside bulkHeadFallback, cause - $t")
        println("Inside bulkHeadFallback method. Some error occurred while calling service for seller registration")
        throw t
    }

    fun saveOrderFallBack(exception: RequestNotPermitted) {
        logger.info("Rate limit has applied, So no further calls are getting accepted")
        logger.warn("Too many requests : No further request will be accepted. Please try after sometime")
        throw exception
    }

    fun saveOrderRetryFallBack(exception: Exception) {
        logger.warn("Retry fallback : No further request will be accepted. ${LocalDateTime.now()}")
        throw exception
    }

    fun listOrders() = listOfOrders
}