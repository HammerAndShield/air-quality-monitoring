package integration

import org.hammernshield.*
import org.hammernshield.AirQualityRecord
import org.hammernshield.Pollutant
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

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

}