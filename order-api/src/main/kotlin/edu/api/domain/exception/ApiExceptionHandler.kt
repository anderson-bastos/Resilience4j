package edu.api.domain.exception

import io.github.resilience4j.bulkhead.BulkheadFullException
import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import io.github.resilience4j.retry.MaxRetriesExceededException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus


@ControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(CallNotPermittedException::class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    fun handleCallNotPermittedException(callNotPermittedException: CallNotPermittedException) {
        println(callNotPermittedException.message)
    }

    @ExceptionHandler(MaxRetriesExceededException::class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    fun handleCallMaxRetriesExceededException(maxRetriesExceededException: MaxRetriesExceededException) {
        println(maxRetriesExceededException.message)
    }

    @ExceptionHandler(BulkheadFullException::class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    fun handleBulkheadFullException(bulkheadFullException: BulkheadFullException) {
        println(bulkheadFullException.message)
    }
}