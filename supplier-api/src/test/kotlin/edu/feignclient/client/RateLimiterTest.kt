package edu.feignclient.client

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import edu.supplier.client.PersonClient
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.function.Consumer


@SpringBootTest("server.port:0")
class RateLimiterTest {

    @RegisterExtension
    var USER_SESSION_SERVICE = WireMockExtension.newInstance()
        .options(WireMockConfiguration.wireMockConfig().port(8081))
        .build()

    @Autowired
    private lateinit var personClient: PersonClient

    @Test
    @Throws(Exception::class)
    fun testRateLimiterWorks() {

        USER_SESSION_SERVICE.stubFor(
            get(urlPathEqualTo("/stores"))
                .willReturn(
                    aResponse().withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withFixedDelay(500)
                )
        )

        val executorService = Executors.newFixedThreadPool(10)

        val futures: MutableList<Future<*>> = ArrayList<Future<*>>()
        for (i in 0..4) {
            val f: Future<*> = executorService.submit { personClient.getTasks() }
            futures.add(f)
        }
        futures.forEach(Consumer { f: Future<*> ->
            try {
                f.get()
            } catch (e: java.lang.Exception) {
                throw RuntimeException(e)
            }
        })


    }
}