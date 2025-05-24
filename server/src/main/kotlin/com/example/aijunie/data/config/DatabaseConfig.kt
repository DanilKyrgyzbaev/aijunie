package com.example.aijunie.data.config

import com.example.aijunie.data.model.UserTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseConfig {
    fun init(
        jdbcUrl: String,
        driverClassName: String,
        username: String,
        password: String
    ) {
        // Connect to the database
        Database.connect(
            url = jdbcUrl,
            driver = driverClassName,
            user = username,
            password = password
        )

        // Create tables
        transaction {
            SchemaUtils.create(UserTable)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block()
    }
}
