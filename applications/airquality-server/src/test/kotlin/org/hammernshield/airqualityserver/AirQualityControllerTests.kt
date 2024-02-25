package org.hammernshield.airqualityserver


import org.hammernshield.AirQualityData
import org.hammernshield.AirQualityDataService
import org.hammernshield.PollutantData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.mockito.Mockito.`when`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@WebMvcTest(AirQualityController::class)
class AirQualityControllerTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var airQualityService: AirQualityDataService

    @BeforeEach
    fun setupMock() {

    }

    @Test
    fun `getMostRecentAirQualityRecord returns most recent record when found`() {
        val expectedData = AirQualityData(100, listOf(), LocalDateTime.now())

        `when`(airQualityService.getMostRecentAirQualityRecord()).thenReturn(expectedData)

        mockMvc.perform(get("/air-quality/"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun `getMostRecentAirQualityRecord returns not found when service throws exception`() {
        `when`(airQualityService.getMostRecentAirQualityRecord()).thenThrow(RuntimeException::class.java)

        mockMvc.perform(get("/air-quality/"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `getPast3DaysAirQualityRecords returns records when found`() {
        val expectedRecords = listOf(
            AirQualityData(100, listOf(), LocalDateTime.now().minusDays(1)),
            AirQualityData(101, listOf(), LocalDateTime.now().minusDays(2)),
            AirQualityData(102, listOf(), LocalDateTime.now().minusDays(3))
        )

        `when`(airQualityService.getPast3DaysAirQualityData()).thenReturn(expectedRecords)

        mockMvc.perform(get("/air-quality/recent"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun `getPast3DaysAirQualityRecords returns not found when service throws exception`() {
        `when`(airQualityService.getPast3DaysAirQualityData()).thenThrow(RuntimeException::class.java)

        mockMvc.perform(get("/air-quality/recent"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `getXAirQualityRecords returns records when found`() {
        val x = 3
        val expectedRecords = listOf(
            AirQualityData(100, listOf(), LocalDateTime.now().minusDays(1)),
            AirQualityData(101, listOf(), LocalDateTime.now().minusDays(2)),
            AirQualityData(102, listOf(), LocalDateTime.now().minusDays(3))
        )

        `when`(airQualityService.getXPastRecords(x)).thenReturn(expectedRecords)

        mockMvc.perform(get("/air-quality/records?x=$x"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun `getXAirQualityRecords returns not found when service throws exception`() {
        val x = 3
        `when`(airQualityService.getXPastRecords(x)).thenThrow(RuntimeException::class.java)

        mockMvc.perform(get("/air-quality/records?x=$x"))
            .andExpect(status().isNotFound)
    }

}