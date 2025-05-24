package com.example.aijunie.domain.usecase

import com.example.aijunie.domain.model.User
import com.example.aijunie.domain.repository.UserRepository
import java.util.UUID

class RefreshTokenUseCase(private val userRepository: UserRepository) {
    suspend fun execute(userId: UUID): User? {
        return userRepository.getUserById(userId)
    }
}