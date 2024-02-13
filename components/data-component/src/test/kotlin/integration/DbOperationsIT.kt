package integration

import org.hammernshield.*
import org.hammernshield.AirQualityRecord
import org.hammernshield.Pollutant
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDateTime

@Tag("integrationTest")
@Testcontainers
class DbOperationsIT {

    lateinit var dataService: AirQualityDataService

    companion object {
        @Container
        val postgresqlContainer = PostgreSQLContainer<Nothing>("postgres:latest").apply {
            withDatabaseName("testdb")
            withUsername("test")
            withPassword("test")
        }
    }

    @BeforeEach
    fun setup() {
        Database.connect(
            url = postgresqlContainer.jdbcUrl,
            driver = "org.postgresql.Driver",
            user = "test",
            password = "test"
        )

        transaction {
            SchemaUtils.drop(AirQualityRecord, Pollutant)
            SchemaUtils.create(AirQualityRecord, Pollutant)
        }

        dataService = AirQualityDataServiceImpl()
    }

    @Test
    fun testInsertAirQualityData() {
        val testData = AirQualityData(
            overallAqi = 35,
            pollutants = listOf(
                PollutantData(name = "CO", concentration = 287.06f, aqi = 3),
            )
        )

        dataService.insertAirQualityData(testData)

        transaction {
            val query = AirQualityRecord.selectAll()
            assertEquals(1L, query.count())

            val pollutantsCount = Pollutant.selectAll().count()
            assertEquals(testData.pollutants.size.toLong(), pollutantsCount)
        }

    }

    @Test
    fun testGetPast3DaysAirQualityData() {
        val now = LocalDateTime.now()

        val recentRecordId = transaction {
            val recordId = AirQualityRecord.insertAndGetId {
                it[overallAqi] = 50
                it[timestamp] = now.minusDays(1)
            }
            Pollutant.batchInsert(listOf("PM2.5" to 35.5f, "PM10" to 40.0f)) { (name, concentration) ->
                this[Pollutant.recordId] = recordId.value
                this[Pollutant.name] = name
                this[Pollutant.concentration] = concentration
                this[Pollutant.aqi] = 50
            }
            recordId.value
        }

        transaction {
            val recordId = AirQualityRecord.insertAndGetId {
                it[overallAqi] = 70
                it[timestamp] = now.minusDays(4)
            }
            Pollutant.insert {
                it[Pollutant.recordId] = recordId.value
                it[name] = "O3"
                it[concentration] = 180.0f
                it[aqi] = 70
            }
        }

        val airQualityDataList = dataService.getPast3DaysAirQualityData()

        assertAll("Verify correct retrieval and data mapping",
            { assertEquals(1, airQualityDataList.size, "Should only retrieve records from the past 3 days.") },
            {
                val retrievedData = airQualityDataList.first()
                assertEquals(50, retrievedData.overallAqi)
                assertEquals(2, retrievedData.pollutants.size, "Should retrieve correct number of pollutants for the recent record.")
            },
            {
                val pollutants = airQualityDataList.first().pollutants
                assertTrue(pollutants.any { it.name == "PM2.5" && it.concentration == 35.5f && it.aqi == 50 })
                assertTrue(pollutants.any { it.name == "PM10" && it.concentration == 40.0f && it.aqi == 50 })
            }
        )
    }

}