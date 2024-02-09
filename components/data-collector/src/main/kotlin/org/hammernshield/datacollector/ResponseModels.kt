package org.hammernshield.datacollector

import com.fasterxml.jackson.annotation.JsonProperty

data class ApiNinjasAirQualityResponse(
    val CO: Pollutant,
    val NO2: Pollutant,
    val O3: Pollutant,
    val SO2: Pollutant,
    @JsonProperty("PM2.5") val PM2_5: Pollutant,
    val PM10: Pollutant,
    val overall_aqi: Int
)

data class Pollutant(
    val concentration: Float,
    val aqi: Int
)