package com.example.aijunie.data.model

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object UserTable : UUIDTable("users") {
    val name = varchar("name", 50)
    val lastName = varchar("last_name", 50)
    val email = varchar("email", 100).uniqueIndex()
    val passwordHash = text("password_hash")
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}
