package org.hammernshield

interface AirQualityDataService {

    fun insertAirQualityData(airQualityData: AirQualityData)
    fun getMostRecentAirQualityRecord(): AirQualityData
    fun getPast3DaysAirQualityData(): List<AirQualityData>
    fun getXPastRecords(x: Int): List<AirQualityData>

}