package org.hammernshield

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database

object DatabaseConfig {

    fun init() {
        val config = HikariConfig().apply {
            jdbcUrl = System.getenv("DB_URL") ?: throw IllegalStateException("DB_URL not found")
            driverClassName = "org.postgresql.Driver"
            username = System.getenv("DB_USER") ?: throw IllegalStateException("DB_USER not found")
            password = System.getenv("DB_PASSWORD") ?: throw IllegalStateException("DB_PASSWORD not found")
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)
    }
}
