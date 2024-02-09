package org.hammernshield.datacollector

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class AirQualityCollectorTest {

    private lateinit var airQualityCollector: AirQualityCollector

    @BeforeEach
    fun setUp() {
        val mockEngine = MockEngine { request ->
            respond(
                content = """
                {
                    "CO": {"concentration": 0.0, "aqi": 0},
                    "NO2": {"concentration": 0.0, "aqi": 0},
                    "O3": {"concentration": 0.0, "aqi": 0},
                    "SO2": {"concentration": 0.0, "aqi": 0},
                    "PM2.5": {"concentration": 0.0, "aqi": 0},
                    "PM10": {"concentration": 0.0, "aqi": 0},
                    "overall_aqi": 50
                }
            """.trimIndent(),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        airQualityCollector = AirQualityCollector(
            "dummyApiKey",
            HttpClient(mockEngine) {
                install(ContentNegotiation) {
                    jackson()
                }
            }
        )
    }

    @Test
    fun `fetchAirQuality returns valid response`() = runBlocking {
        val response = airQualityCollector.fetchAirQuality(36.198870, -115.262260)
        assertEquals(50, response.overall_aqi)
    }

}