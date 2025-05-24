package com.example.aijunie.data.model

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import java.util.UUID

class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserEntity>(UserTable)

    var name by UserTable.name
    var lastName by UserTable.lastName
    var email by UserTable.email
    var passwordHash by UserTable.passwordHash
    var createdAt by UserTable.createdAt
    var updatedAt by UserTable.updatedAt
}
