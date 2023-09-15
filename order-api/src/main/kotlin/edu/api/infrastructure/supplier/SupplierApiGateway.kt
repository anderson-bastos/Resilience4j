package edu.api.infrastructure.supplier

import edu.api.domain.entity.Order
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


@FeignClient("supplier-api", url = "http://localhost:7701")
interface SupplierApiGateway {

    @PostMapping("/order")
    fun saveOrder(@RequestBody order: Order)

    @GetMapping("/order")
    fun listOrders(): List<Order>?

}