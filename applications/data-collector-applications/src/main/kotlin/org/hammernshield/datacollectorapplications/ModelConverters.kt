package org.hammernshield.datacollectorapplications

import org.hammernshield.AirQualityData
import org.hammernshield.PollutantData
import org.hammernshield.datacollector.ApiNinjasAirQualityResponse

fun convertToAirQualityData(response: ApiNinjasAirQualityResponse): AirQualityData {
    val pollutants = listOf(
        PollutantData("CO", response.CO.concentration, response.CO.aqi),
        PollutantData("NO2", response.NO2.concentration, response.NO2.aqi),
        PollutantData("O3", response.O3.concentration, response.O3.aqi),
        PollutantData("SO2", response.SO2.concentration, response.SO2.aqi),
        PollutantData("PM2.5", response.PM2_5.concentration, response.PM2_5.aqi),
        PollutantData("PM10", response.PM10.concentration, response.PM10.aqi),
    )
    return AirQualityData(response.overall_aqi, pollutants)
}
