package edu.supplier

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class SupplierApiApplication

fun main(args: Array<String>) {
	runApplication<SupplierApiApplication>(*args)
}
