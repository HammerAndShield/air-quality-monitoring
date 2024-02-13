package org.hammernshield

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

internal object AirQualityRecord : IntIdTable() {
    val overallAqi = integer("overallaqi")
    val timestamp = datetime("timestamp")
}

internal object Pollutant : IntIdTable() {
    val recordId = integer("recordid").references(AirQualityRecord.id)
    val name = varchar("name", length = 50)
    val concentration = float("concentration")
    val aqi = integer("aqi")
}