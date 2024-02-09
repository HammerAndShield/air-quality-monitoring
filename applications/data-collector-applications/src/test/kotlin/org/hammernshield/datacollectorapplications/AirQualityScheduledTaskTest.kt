package org.hammernshield.datacollectorapplications

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import net.bytebuddy.matcher.ElementMatchers.any
import org.hammernshield.AirQualityData
import org.hammernshield.AirQualityDataService
import org.hammernshield.datacollector.AirQualityCollector
import org.hammernshield.datacollector.ApiNinjasAirQualityResponse
import org.hammernshield.datacollector.Pollutant
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.platform.commons.logging.LoggerFactory
import org.springframework.context.event.ContextRefreshedEvent

class AirQualityScheduledTaskTest {

    private lateinit var airQualityScheduledTask: AirQualityScheduledTask
    private val airQualityCollector = mockk<AirQualityCollector>()
    private val airQualityDataService = mockk<AirQualityDataService>(relaxed = true)

    @BeforeEach
    fun setUp() {
        airQualityScheduledTask = AirQualityScheduledTask(airQualityCollector, airQualityDataService)
    }

    @Test
    fun `fetchAndStoreAirQuality fetches and stores air quality data successfully`() = runBlocking {
        val dummyResponse = ApiNinjasAirQualityResponse(
            CO = Pollutant(concentration = 0.1f, aqi = 1),
            NO2 = Pollutant(concentration = 0.2f, aqi = 2),
            O3 = Pollutant(concentration = 0.3f, aqi = 3),
            SO2 = Pollutant(concentration = 0.4f, aqi = 4),
            PM2_5 = Pollutant(concentration = 0.5f, aqi = 5),
            PM10 = Pollutant(concentration = 0.6f, aqi = 6),
            overall_aqi = 50
        )

        val slot = slot<AirQualityData>()
        coEvery { airQualityCollector.fetchAirQuality(any(), any()) } returns dummyResponse
        coEvery { airQualityDataService.insertAirQualityData(capture(slot)) } answers { Unit }

        airQualityScheduledTask.onApplicationEvent(ContextRefreshedEvent(mockk()))

        coVerify { airQualityCollector.fetchAirQuality(36.198870, -115.262260) }
        coVerify(exactly = 1) { airQualityDataService.insertAirQualityData(slot.captured) }

        assertEquals(50, slot.captured.overallAqi)
    }

    @Test
    fun `fetchAndStoreAirQuality handles fetch errors gracefully`() = runBlocking {
        coEvery { airQualityCollector.fetchAirQuality(any(), any()) } throws Exception("API failure")

        airQualityScheduledTask.onApplicationEvent(ContextRefreshedEvent(mockk()))

        coVerify { airQualityCollector.fetchAirQuality(36.198870, -115.262260) }
        coVerify(exactly = 0) { airQualityDataService.insertAirQualityData(any()) }
    }

}