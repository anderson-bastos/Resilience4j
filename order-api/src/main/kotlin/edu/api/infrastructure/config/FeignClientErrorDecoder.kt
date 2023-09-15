package edu.api.infrastructure.config

import feign.Response
import feign.RetryableException
import feign.codec.ErrorDecoder
import java.time.LocalDateTime


class FeignClientErrorDecoder :ErrorDecoder {
    override fun decode(methodKey: String, response: Response): Exception {
        val defaultErrorDecoder = ErrorDecoder.Default().decode(methodKey, response)

        println("${response.status()}, ${response.reason()}, ${response.request().httpMethod()}, ${response.request()}")

        return when(response.status()) {
            in 400..511 -> RetryableException(
                response.status(), response.reason(), response.request().httpMethod(), null, response.request()
            )
            else -> defaultErrorDecoder
        }
    }
}