package org.hammernshield

data class AirQualityData(
    val overallAqi: Int,
    val pollutants: List<PollutantData>
)

data class PollutantData(
    val name: String,
    val concentration: Float,
    val aqi: Int
)