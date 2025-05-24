package com.example.aijunie.domain.usecase

import com.example.aijunie.domain.repository.UserRepository
import java.util.UUID

class ChangePasswordUseCase(private val userRepository: UserRepository) {
    suspend fun execute(
        userId: UUID, 
        currentPassword: String, 
        newPassword: String, 
        passwordVerifier: (String, String) -> Boolean,
        passwordHasher: (String) -> String
    ): Boolean {
        val user = userRepository.getUserById(userId) ?: return false
        val email = user.email
        val passwordHash = userRepository.getPasswordHashByEmail(email) ?: return false

        if (!passwordVerifier(currentPassword, passwordHash)) {
            return false
        }

        val newPasswordHash = passwordHasher(newPassword)
        return userRepository.updatePassword(userId, newPasswordHash)
    }
}
