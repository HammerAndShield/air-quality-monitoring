package org.hammernshield

interface AirQualityDataService {

    fun insertAirQualityData(airQualityData: AirQualityData)
    fun getPast3DaysAirQualityData(): List<AirQualityData>
}