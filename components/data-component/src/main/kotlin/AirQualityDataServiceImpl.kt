package org.hammernshield

import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
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

    override fun getMostRecentAirQualityRecord(): AirQualityData {
        return transaction {
            val mostRecentRecord = AirQualityRecord.selectAll()
                .orderBy(AirQualityRecord.timestamp, SortOrder.DESC)
                .limit(1)
                .firstOrNull()

            mostRecentRecord?.let { record ->
                val recordId = record[AirQualityRecord.id].value
                val overallAqi = record[AirQualityRecord.overallAqi]
                val timestamp = record[AirQualityRecord.timestamp]

                val pollutants = Pollutant.select { Pollutant.recordId eq recordId }.map { pollutant ->
                    PollutantData(
                        name = pollutant[Pollutant.name],
                        concentration = pollutant[Pollutant.concentration],
                        aqi = pollutant[Pollutant.aqi]
                    )
                }

                AirQualityData(overallAqi, pollutants, timestamp)
            } ?: throw NoSuchElementException("No air quality record found")
        }
    }


    override fun getPast3DaysAirQualityData(): List<AirQualityData> {
        return transaction {
            val threeDaysAgo = LocalDateTime.now().minusDays(3)
            AirQualityRecord.select {
                AirQualityRecord.timestamp greaterEq threeDaysAgo
            }.orderBy(AirQualityRecord.timestamp, SortOrder.DESC)
                .map { record ->
                    val recordId = record[AirQualityRecord.id].value
                    val overallAqi = record[AirQualityRecord.overallAqi]
                    val timestamp = record[AirQualityRecord.timestamp]
                    val pollutants = Pollutant.select { Pollutant.recordId eq recordId }
                        .map { pollutant ->
                            PollutantData(
                                name = pollutant[Pollutant.name],
                                concentration = pollutant[Pollutant.concentration],
                                aqi = pollutant[Pollutant.aqi]
                            )
                        }
                    AirQualityData(overallAqi, pollutants, timestamp)
                }
        }
    }


    override fun getXPastRecords(x: Int): List<AirQualityData> {
        return transaction {
            AirQualityRecord.selectAll()
                .orderBy(AirQualityRecord.timestamp, SortOrder.DESC) // Sort by timestamp instead of id for consistency
                .limit(x)
                .map { record ->
                    val recordId = record[AirQualityRecord.id].value
                    val overallAqi = record[AirQualityRecord.overallAqi]
                    val timestamp = record[AirQualityRecord.timestamp] // Fetch the timestamp
                    val pollutants = Pollutant.select { Pollutant.recordId eq recordId }
                        .map { pollutant ->
                            PollutantData(
                                name = pollutant[Pollutant.name],
                                concentration = pollutant[Pollutant.concentration],
                                aqi = pollutant[Pollutant.aqi]
                            )
                        }
                    AirQualityData(overallAqi, pollutants, timestamp)
                }
        }
    }


}



