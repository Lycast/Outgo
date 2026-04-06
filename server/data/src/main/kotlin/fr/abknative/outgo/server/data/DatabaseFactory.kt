package fr.abknative.outgo.server.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.v1.jdbc.Database

/**
 * Singleton responsible for initializing the database connection pool
 * and running Flyway migrations.
 * * It requires DB_URL, DB_USER, and DB_PASSWORD environment variables
 * to be strictly defined in the system.
 */
object DatabaseFactory {
    fun init() {
        val dbUrl = requireNotNull(System.getenv("DB_URL")) { "Environment variable DB_URL is missing" }
        val dbUser = requireNotNull(System.getenv("DB_USER")) { "Environment variable DB_USER is missing" }
        val dbPassword = requireNotNull(System.getenv("DB_PASSWORD")) { "Environment variable DB_PASSWORD is missing" }

        val config = HikariConfig().apply {
            jdbcUrl = dbUrl
            username = dbUser
            password = dbPassword
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        val dataSource = HikariDataSource(config)

        Flyway.configure()
            .dataSource(dataSource)
            .load()
            .migrate()

        Database.connect(dataSource)
    }
}