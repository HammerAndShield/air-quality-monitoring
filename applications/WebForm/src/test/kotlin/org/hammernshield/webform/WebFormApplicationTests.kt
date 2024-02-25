package org.hammernshield.webform

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hammernshield.AirQualityData
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.ui.Model
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@SpringBootTest
class WebFormApplicationTests {
    private lateinit var webClientBuilder: WebClient.Builder
    private lateinit var webClient: WebClient
    private lateinit var model: Model
    private lateinit var controller: AirQualityFormController

    @BeforeEach
    fun setUp() {
        webClient = mockk()
        webClientBuilder = mockk()
        model = mockk(relaxed = true)

        every { webClientBuilder.build() } returns webClient

        controller = AirQualityFormController(webClientBuilder)
    }

    @Test
    fun `homeRequest should add air quality data to model when successful`() {
        val expectedAirQualityData = AirQualityData(100, listOf(), LocalDateTime.now())
        val monoAirQualityData = Mono.just(expectedAirQualityData)

        every {
            webClient.get()
                .uri(any<String>())
                .retrieve()
                .bodyToMono(AirQualityData::class.java)
        } returns monoAirQualityData

        val viewName = controller.homeRequest(model)

        assertEquals("home", viewName)
        verify { model.addAttribute("airQualityData", expectedAirQualityData) }
    }

    @Test
    fun `homeRequest should handle errors gracefully`() {
        every {
            webClient.get()
                .uri(any<String>())
                .retrieve()
                .bodyToMono(AirQualityData::class.java)
        } returns Mono.error(RuntimeException("Failed to fetch"))

        val viewName = controller.homeRequest(model)

        assertEquals("home", viewName)
        io.mockk.verify { model.addAttribute("error", "Failed to fetch air quality data.") }
    }

    @Test
    fun `latestRequest should add air quality data list and average AQI to model when successful`() {
        val airQualityDataList = listOf(
            AirQualityData(100, listOf(), LocalDateTime.now()),
            AirQualityData(80, listOf(), LocalDateTime.now())
        )
        val fluxAirQualityData: Flux<AirQualityData> = Flux.fromIterable(airQualityDataList)

        every {
            webClient.get()
                .uri(any<String>())
                .retrieve()
                .bodyToFlux(AirQualityData::class.java)
        } returns fluxAirQualityData

        val viewName = controller.latestRequest(model)

        assertEquals("latest", viewName)
        verify { model.addAttribute("airQualityDataList", airQualityDataList) }
        verify { model.addAttribute("averageAqi", 90) } // Assuming the average calculation is correct
    }

    @Test
    fun `latestRequest should handle errors gracefully`() {
        every {
            webClient.get()
                .uri(any<String>())
                .retrieve()
                .bodyToFlux(AirQualityData::class.java)
        } returns Flux.error(RuntimeException("Failed to fetch"))

        val viewName = controller.latestRequest(model)

        assertEquals("latest", viewName)
        verify { model.addAttribute("error", "Failed to fetch air quality data.") }
    }

    @Test
    fun `bulkRequest should add air quality data list, average AQI, and record count to model when successful`() {
        val amount = 5
        val airQualityDataList = List(amount) {
            AirQualityData(90 + it * 10, listOf(), LocalDateTime.now().minusDays(it.toLong()))
        }
        val fluxAirQualityData: Flux<AirQualityData> = Flux.fromIterable(airQualityDataList)
        val expectedAverageAqi = airQualityDataList.map { it.overallAqi }.average().toInt()

        every {
            webClient.get()
                .uri(any<String>())
                .retrieve()
                .bodyToFlux(AirQualityData::class.java)
        } returns fluxAirQualityData

        val viewName = controller.bulkRequest(amount, model)

        assertEquals("latest", viewName)
        verify { model.addAttribute("airQualityDataList", airQualityDataList) }
        verify { model.addAttribute("averageAqi", expectedAverageAqi) }
        verify { model.addAttribute("recordCount", amount) }
    }

    @Test
    fun `bulkRequest should handle errors gracefully`() {
        val amount = 3

        every {
            webClient.get()
                .uri(any<String>())
                .retrieve()
                .bodyToFlux(AirQualityData::class.java)
        } returns Flux.error(RuntimeException("Failed to fetch"))

        val viewName = controller.bulkRequest(amount, model)

        assertEquals("latest", viewName)
        verify { model.addAttribute("error", "Failed to fetch air quality data.") }
    }

}
