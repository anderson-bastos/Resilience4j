package edu.supplier.application.web

import edu.supplier.domain.entity.Order
import edu.supplier.domain.service.OrderService
import io.github.resilience4j.ratelimiter.RequestNotPermitted
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import io.github.resilience4j.retry.annotation.Retry
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Duration
import java.time.LocalDateTime


@RestController
class OrderController(
    private val orderService: OrderService
) {

    private val logger = LoggerFactory.getLogger(OrderController::class.java)

    @PostMapping("/order")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveOrder(@RequestBody order: Order) {
        orderService.save(order)
    }

    @GetMapping("/order")
    @ResponseStatus(HttpStatus.OK)
    fun listOrders(): List<Order> {
        return orderService.listOrders().sortedBy { it.thread }
    }

    @GetMapping("/order/size")
    @ResponseStatus(HttpStatus.OK)
    fun size(): Int {
        return orderService.listOrders().size
    }
}