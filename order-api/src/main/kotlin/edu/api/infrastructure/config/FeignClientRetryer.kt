package edu.api.infrastructure.config

import feign.RetryableException
import feign.Retryer
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class FeignClientRetryer(
    @Value("\${feign.client.config.default.readTimeout}") private val readTimeout: Long
): Retryer {

    override fun continueOrPropagate(e: RetryableException) {
        throw e
    }

    override fun clone(): Retryer {
        return Retryer.Default(500, Duration.ofMinutes(6).toMillis(), 5)
    }
}