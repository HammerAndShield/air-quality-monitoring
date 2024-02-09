package org.hammernshield

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

internal object AirQualityRecord : Table() {
    val id = integer("id").autoIncrement()
    val overallAqi = integer("overallaqi")
    val timestamp = datetime("timestamp")
    override val primaryKey = PrimaryKey(id)
}

internal object Pollutant : Table() {
    val id = integer("id").autoIncrement()
    val recordId = integer("recordid").references(AirQualityRecord.id)
    val name = varchar("name", length = 50)
    val concentration = float("concentration")
    val aqi = integer("aqi")
    override val primaryKey = PrimaryKey(id)
}