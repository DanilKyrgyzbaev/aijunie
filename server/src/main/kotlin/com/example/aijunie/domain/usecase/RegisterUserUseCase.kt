package com.example.aijunie.domain.usecase

import com.example.aijunie.domain.model.User
import com.example.aijunie.domain.repository.UserRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

class RegisterUserUseCase(private val userRepository: UserRepository) {
    suspend fun execute(name: String, lastName: String, email: String, password: String, passwordHasher: (String) -> String): User {
        val user = User(
            id = UUID.randomUUID(),
            name = name,
            lastName = lastName,
            email = email,
            createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            updatedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )

        val passwordHash = passwordHasher(password)
        return userRepository.createUser(user, passwordHash)
    }
}
