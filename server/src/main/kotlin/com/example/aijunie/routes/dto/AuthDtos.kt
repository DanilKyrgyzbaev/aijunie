package com.example.aijunie.routes.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val name: String,
    val lastName: String,
    val email: String,
    val password: String
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)

@Serializable
data class ChangePasswordRequest(
    val userId: String,
    val currentPassword: String,
    val newPassword: String
)

@Serializable
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: String
)

@Serializable
data class UserResponse(
    val id: String,
    val name: String,
    val lastName: String,
    val email: String
)

@Serializable
data class ErrorResponse(
    val message: String
)
