package edu.api.application.web

import edu.api.domain.entity.Order
import edu.api.domain.service.OrderService
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping("/order")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveOrder(@RequestBody order: Order) {
        orderService.saveOrder(order)
    }


    @GetMapping("/order")
    //@CircuitBreaker(name = "CircuitBreakerPersonService")
    //@RateLimiter(name = "CircuitBreakerPersonService")
    fun getOrders() = orderService.listOrders()
}