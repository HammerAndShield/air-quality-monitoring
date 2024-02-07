package org.hammernshield

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

internal object AirQualityRecord : Table() {
    val id = integer("ID").autoIncrement()
    val overallAqi = integer("OverallAQI")
    val timestamp = datetime("Timestamp")
}

internal object Pollutant : Table() {
    val id = integer("ID").autoIncrement()
    val recordId = integer("RecordId").references(AirQualityRecord.id)
    val name = varchar("Name", length = 50)
    val concentration = float("Concentration")
    val aqi = integer("AQI")
}