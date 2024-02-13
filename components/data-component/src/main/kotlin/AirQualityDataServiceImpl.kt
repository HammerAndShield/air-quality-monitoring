package org.hammernshield

import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
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
                    it[recordId] = airQualityRecordId.value
                    it[name] = pollutant.name
                    it[concentration] = pollutant.concentration
                    it[aqi] = pollutant.aqi
                }
            }
        }
    }

    override fun getPast3DaysAirQualityData(): List<AirQualityData> {
         return transaction {
             val airQualityDataMap = mutableMapOf<Int, AirQualityData>()
             val threeDaysAgo = LocalDateTime.now().minusDays(3)

             val airqualityRecords = AirQualityRecord.select {
                 AirQualityRecord.timestamp greaterEq threeDaysAgo
             }.orderBy(AirQualityRecord.timestamp, SortOrder.DESC)

             airqualityRecords.forEach { record ->
                 val recordId = record[AirQualityRecord.id].value
                 val overallAqi = record[AirQualityRecord.overallAqi]

                 val pollutants = Pollutant.select { Pollutant.recordId eq recordId }.map { pollutant ->
                     PollutantData(
                         name = pollutant[Pollutant.name],
                         concentration = pollutant[Pollutant.concentration],
                         aqi = pollutant[Pollutant.aqi]
                     )
                 }

                 airQualityDataMap[recordId] = AirQualityData(overallAqi, pollutants)

             }

             airQualityDataMap.values.toList()
        }
    }

}



