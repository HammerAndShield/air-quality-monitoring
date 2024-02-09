package org.hammernshield

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class AirQualityDataServiceImpl : AirQualityDataService {

    override fun insertAirQualityData(airQualityData: AirQualityData) {
        transaction {
            val airQualityRecordId = AirQualityRecord.insert {
                it[overallAqi] = airQualityData.overallAqi
                it[timestamp] = LocalDateTime.now()
            } get AirQualityRecord.id

            airQualityData.pollutants.forEach { pollutant ->
                Pollutant.insert {
                    it[recordId] = airQualityRecordId
                    it[name] = pollutant.name
                    it[concentration] = pollutant.concentration
                    it[aqi] = pollutant.aqi
                }
            }
        }
    }

}



