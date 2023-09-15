package edu.api.application.config

import io.github.resilience4j.bulkhead.BulkheadFullException
import io.github.resilience4j.retry.Retry
import io.github.resilience4j.retry.RetryConfig
import io.github.resilience4j.retry.RetryRegistry
import org.springframework.boot.actuate.trace.http.HttpTrace.Response
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.IOException
import java.time.Duration
import java.util.concurrent.TimeoutException


@Configuration
class RetryConfigResillience4j {
    @Bean
    fun retrySupplierApi(): Retry {
        val config = RetryConfig.custom<Any>()
            .maxAttempts(5)
            .waitDuration(Duration.ofMillis(1000))
            .retryOnException { e: Throwable? -> e is Exception }
            .retryExceptions(
                IOException::class.java,
                TimeoutException::class.java,
                Exception::class.java,
                BulkheadFullException::class.java
            )
            .failAfterMaxAttempts(true)
            .build()
        return RetryRegistry.of(config).retry("retrySupplierApi")
    }
}