package com.example.aijunie

import com.example.aijunie.data.config.DatabaseConfig
import com.example.aijunie.di.appModule
import com.example.aijunie.plugins.configureMonitoring
import com.example.aijunie.plugins.configureRouting
import com.example.aijunie.plugins.configureSecurity
import com.example.aijunie.plugins.configureSerialization
import com.example.aijunie.plugins.configureStatusPages
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.context.startKoin
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main() {
    // Initialize Koin
    startKoin {
        slf4jLogger()
        modules(appModule)
    }
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080
    embeddedServer(Netty, port = port, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    // Install Koin
    install(Koin) {
        modules(appModule)
    }

    // Configure database
    try {
        // First try to read from environment variables
        val jdbcUrl = System.getenv("DATABASE_URL")
        val driverClassName = "org.postgresql.Driver" // Assuming PostgreSQL for Docker
        val username = System.getenv("DATABASE_USERNAME")
        val password = System.getenv("DATABASE_PASSWORD")

        if (jdbcUrl != null && username != null && password != null) {
            // Use environment variables if available
            DatabaseConfig.init(jdbcUrl, driverClassName, username, password)
        } else {
            // Fall back to application.conf
            val dbConfig = environment.config.config("database")
            val configJdbcUrl = dbConfig.property("jdbcUrl").getString()
            val configDriverClassName = dbConfig.property("driverClassName").getString()
            val configUsername = dbConfig.property("username").getString()
            val configPassword = dbConfig.property("password").getString()

            DatabaseConfig.init(configJdbcUrl, configDriverClassName, configUsername, configPassword)
        }
    } catch (e: Exception) {
        // Fall back to H2 in-memory database
        val jdbcUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
        val driverClassName = "org.h2.Driver"
        val username = "sa"
        val password = ""

        DatabaseConfig.init(jdbcUrl, driverClassName, username, password)
    }

    // Порядок имеет значение
    configureSerialization()   // сначала сериализация!
    configureStatusPages()     // затем обработка ошибок
    configureMonitoring()
    configureSecurity()
    configureRouting()

    routing {
        get("/") {
            call.respondText("Ktor работает!")
        }
    }
}
