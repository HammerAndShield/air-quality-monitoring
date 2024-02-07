package org.hammernshield

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database

object DatabaseConfig {
    private val dotenv = dotenv()

    fun init() {
        val config = HikariConfig().apply {
            jdbcUrl = dotenv["DB_URL"] ?: throw IllegalStateException("DB_URL not found in .env file")
            driverClassName = "org.postgresql.Driver"
            username = dotenv["DB_USER"] ?: throw IllegalStateException("DB_USER not found in .env file")
            password = dotenv["DB_PASSWORD"] ?: throw IllegalStateException("DB_PASSWORD not found in .env file")
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)
    }
}
