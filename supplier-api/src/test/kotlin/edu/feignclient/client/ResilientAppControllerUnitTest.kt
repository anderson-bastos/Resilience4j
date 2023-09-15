package edu.feignclient.client

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import com.google.gson.Gson
import edu.supplier.domain.entity.Order
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.function.Consumer


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ResilientAppControllerUnitTest {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var userRegistrationResilience4j: UserRegistrationResilience4j

    @RegisterExtension
    var mockExtension: WireMockExtension = WireMockExtension.newInstance()
        .options(WireMockConfiguration.wireMockConfig().port(9900))
        .build()

    @Test
    fun testCircuitBreaker() {
        mockExtension.stubFor(get("/stores").willReturn(serverError()))

        (1..3).forEach { _ ->
            val response: ResponseEntity<*> = restTemplate.getForEntity("/person", String::class.java)
            Assertions.assertEquals(response.statusCode, HttpStatus.INTERNAL_SERVER_ERROR)
        }

        (1..3).forEach { _ ->
            val response: ResponseEntity<*> = restTemplate.getForEntity("/person", String::class.java)
            Assertions.assertEquals(response.statusCode, HttpStatus.SERVICE_UNAVAILABLE)
        }

        mockExtension.verify(9, getRequestedFor(urlEqualTo("/stores")))
    }

    @Test
    fun testRateLimiter() {
        mockExtension.stubFor(
            get("/stores").willReturn(
               okJson(
                   Gson().toJson(listOf(Order(product = "Test")))
               )
            )
        )

        val executorService = Executors.newFixedThreadPool(10)
        val futures = ArrayList<Future<*>>()
        (1..5).forEach { _ ->
            val f = executorService.submit { restTemplate.getForEntity("/person", String::class.java) }
            futures.add(f)
        }

        futures.forEach(Consumer { f: Future<*> ->
            try {
                f.get()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        })
    }

    @Test
    fun testBulkHead() {

        val executorService = Executors.newFixedThreadPool(10)
        val futures = ArrayList<Future<*>>()
        (1..5).forEach { _ ->
            val f = executorService.submit { restTemplate.getForEntity("/register/seller", String::class.java) }
            futures.add(f)
        }

        futures.forEach(Consumer { f: Future<*> ->
            try {
                f.get()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        })
    }
}