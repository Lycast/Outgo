package fr.abknative.outgo.server.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.v1.jdbc.Database

object DatabaseFactory {
    fun init() {
        val dbUrl = System.getenv("DB_URL") ?: error("La variable d'environnement DB_URL est manquante")
        val dbUser = System.getenv("DB_USER") ?: error("La variable d'environnement DB_USER est manquante")
        val dbPassword = System.getenv("DB_PASSWORD") ?: error("La variable d'environnement DB_PASSWORD est manquante")

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