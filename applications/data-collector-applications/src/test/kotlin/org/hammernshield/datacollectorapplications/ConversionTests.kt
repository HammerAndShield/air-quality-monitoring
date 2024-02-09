package org.hammernshield.datacollectorapplications

import org.hammernshield.PollutantData
import org.hammernshield.datacollector.ApiNinjasAirQualityResponse
import org.hammernshield.datacollector.Pollutant
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ConversionTests {

    @Test
    fun `convertToAirQualityData correctly converts AirNinjasAirQualityResponse to AirQualityData`() {
        val apiReponse = ApiNinjasAirQualityResponse(
            CO = Pollutant(concentration = 230.31f, aqi = 2),
            NO2 = Pollutant(concentration = 2.59f, aqi = 3),
            O3 = Pollutant(concentration = 8.59f, aqi = 4),
            SO2 = Pollutant(concentration = 10.59f, aqi = 12),
            PM2_5 = Pollutant(concentration = 62.5f, aqi = 13),
            PM10 = Pollutant(concentration = 102f, aqi = 4),
            overall_aqi = 121
        )

        val airQualityData = convertToAirQualityData(apiReponse)

        assertEquals(121, airQualityData.overallAqi)
        assertEquals(6, airQualityData.pollutants.size)
        assertTrue(airQualityData.pollutants.any { it.name == "CO" && it.concentration == 230.31f && it.aqi == 2 })
        assertTrue(airQualityData.pollutants.any { it.name == "NO2" && it.concentration == 2.59f && it.aqi == 3 })
        assertTrue(airQualityData.pollutants.any { it.name == "O3" && it.concentration == 8.59f && it.aqi == 4 })
        assertTrue(airQualityData.pollutants.any { it.name == "SO2" && it.concentration == 10.59f && it.aqi == 12 })
        assertTrue(airQualityData.pollutants.any { it.name == "PM2.5" && it.concentration == 62.5f && it.aqi == 13 })
        assertTrue(airQualityData.pollutants.any { it.name == "PM10" && it.concentration == 102f && it.aqi == 4 })

    }

}