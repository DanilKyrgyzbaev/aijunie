package com.example.aijunie.domain.repository

import com.example.aijunie.domain.model.User
import java.util.UUID

interface UserRepository {
    suspend fun getUserById(id: UUID): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun getPasswordHashByEmail(email: String): String?
    suspend fun createUser(user: User, passwordHash: String): User
    suspend fun updateUser(user: User): User
    suspend fun updatePassword(userId: UUID, newPasswordHash: String): Boolean
    suspend fun deleteUser(id: UUID): Boolean
}
