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
        val dbConfig = environment.config.config("database")
        val jdbcUrl = dbConfig.property("jdbcUrl").getString()
        val driverClassName = dbConfig.property("driverClassName").getString()
        val username = dbConfig.property("username").getString()
        val password = dbConfig.property("password").getString()

        DatabaseConfig.init(jdbcUrl, driverClassName, username, password)
    } catch (e: Exception) {
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
