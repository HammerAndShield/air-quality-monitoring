package org.hammernshield

import java.time.LocalDateTime

data class AirQualityData(
    val overallAqi: Int,
    val pollutants: List<PollutantData>,
    val timestamp: LocalDateTime
)

data class PollutantData(
    val name: String,
    val concentration: Float,
    val aqi: Int
)