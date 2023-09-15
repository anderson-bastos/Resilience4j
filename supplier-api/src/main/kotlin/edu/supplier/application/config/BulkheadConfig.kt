package edu.supplier.application.config

import io.github.resilience4j.bulkhead.Bulkhead
import io.github.resilience4j.bulkhead.BulkheadConfig
import io.github.resilience4j.bulkhead.BulkheadRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class BulkheadConfig {

    @Bean
    fun bulkheadSupplierApi() : Bulkhead {
        val config = BulkheadConfig.custom()
            .maxConcurrentCalls(1)
            .maxWaitDuration(Duration.ofMinutes(2))
            .build()
        return BulkheadRegistry.of(config).bulkhead("bulkheadSupplierApi")
    }
}