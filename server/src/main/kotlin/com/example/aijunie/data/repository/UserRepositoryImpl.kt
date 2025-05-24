package com.example.aijunie.data.repository

import com.example.aijunie.data.config.DatabaseConfig.dbQuery
import com.example.aijunie.data.mapper.UserMapper
import com.example.aijunie.data.model.UserEntity
import com.example.aijunie.data.model.UserTable
import com.example.aijunie.domain.model.User
import com.example.aijunie.domain.repository.UserRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserRepositoryImpl : UserRepository {
    override suspend fun getUserById(id: UUID): User? = dbQuery {
        UserEntity.findById(id)?.let { UserMapper.toDomain(it) }
    }

    override suspend fun getUserByEmail(email: String): User? = dbQuery {
        UserEntity.find { UserTable.email eq email }.firstOrNull()?.let { UserMapper.toDomain(it) }
    }

    override suspend fun getPasswordHashByEmail(email: String): String? = dbQuery {
        UserEntity.find { UserTable.email eq email }.firstOrNull()?.passwordHash
    }

    override suspend fun createUser(user: User, passwordHash: String): User = dbQuery {
        val entity = UserMapper.toEntity(user, passwordHash)
        UserMapper.toDomain(entity)
    }

    override suspend fun updateUser(user: User): User = dbQuery {
        val entity = UserEntity.findById(user.id) ?: throw IllegalArgumentException("User not found")
        entity.name = user.name
        entity.lastName = user.lastName
        entity.email = user.email
        entity.updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        UserMapper.toDomain(entity)
    }

    override suspend fun updatePassword(userId: UUID, newPasswordHash: String): Boolean = dbQuery {
        val entity = UserEntity.findById(userId) ?: return@dbQuery false
        entity.passwordHash = newPasswordHash
        entity.updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        true
    }

    override suspend fun deleteUser(id: UUID): Boolean = dbQuery {
        val entity = UserEntity.findById(id) ?: return@dbQuery false
        entity.delete()
        true
    }
}
