package com.example.aijunie.domain.usecase

import com.example.aijunie.domain.model.User
import com.example.aijunie.domain.repository.UserRepository

class LoginUserUseCase(private val userRepository: UserRepository) {
    suspend fun execute(email: String, password: String, passwordVerifier: (String, String) -> Boolean): User? {
        val user = userRepository.getUserByEmail(email) ?: return null
        val passwordHash = userRepository.getPasswordHashByEmail(email) ?: return null

        return if (passwordVerifier(password, passwordHash)) {
            user
        } else {
            null
        }
    }
}
