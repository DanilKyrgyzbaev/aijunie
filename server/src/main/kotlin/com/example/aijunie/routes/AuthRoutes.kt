package com.example.aijunie.routes

import com.example.aijunie.domain.usecase.ChangePasswordUseCase
import com.example.aijunie.domain.usecase.LoginUserUseCase
import com.example.aijunie.domain.usecase.RefreshTokenUseCase
import com.example.aijunie.domain.usecase.RegisterUserUseCase
import com.example.aijunie.exceptions.AuthenticationException
import com.example.aijunie.exceptions.BadRequestException
import com.example.aijunie.exceptions.ValidationException
import com.example.aijunie.routes.dto.*
import com.example.aijunie.security.JwtConfig
import com.example.aijunie.security.PasswordService
import com.example.aijunie.validation.ValidationService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.ContentTransformationException
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import java.util.*

fun Route.authRoutes(
    registerUserUseCase: RegisterUserUseCase,
    loginUserUseCase: LoginUserUseCase,
    refreshTokenUseCase: RefreshTokenUseCase,
    changePasswordUseCase: ChangePasswordUseCase,
    jwtConfig: JwtConfig,
    passwordService: PasswordService
) {
    route("/api/v1/auth") {
        post("/register") {
            try {
                // Log the content type of the request
                val contentType = call.request.contentType()
                println("Content-Type for register: $contentType")

                // Ensure content type is application/json
                if (contentType.toString().lowercase() != ContentType.Application.Json.toString().lowercase()) {
                    throw BadRequestException("Content-Type must be application/json")
                }

                val request = call.receive<RegisterRequest>()

                // Validate request using ValidationService
                val validationErrors = ValidationService.validateRegistration(
                    name = request.name,
                    lastName = request.lastName,
                    email = request.email,
                    password = request.password
                )

                if (validationErrors.isNotEmpty()) {
                    throw ValidationException("Validation failed for registration request", validationErrors)
                }

                // Register user
                val user = registerUserUseCase.execute(
                    name = request.name,
                    lastName = request.lastName,
                    email = request.email,
                    password = request.password,
                    passwordHasher = passwordService::hashPassword
                )

                // Generate tokens
                val accessToken = jwtConfig.createAccessToken(user.id)
                val refreshToken = jwtConfig.createRefreshToken(user.id)

                call.respond(
                    HttpStatusCode.Created, 
                    AuthResponse(
                        accessToken = accessToken,
                        refreshToken = refreshToken,
                        userId = user.id.toString()
                    )
                )
            } catch (e: ContentTransformationException) {
                println("Content transformation error in register: ${e.message}")
                throw BadRequestException("Invalid request format. Please ensure you're sending valid JSON with all required fields.")
            } catch (e: BadRequestException) {
                // Let StatusPages handle this
                throw e
            } catch (e: ValidationException) {
                // Let StatusPages handle this
                throw e
            } catch (e: Exception) {
                println("Registration error: ${e.message}")
                e.printStackTrace()
                throw e
            }
        }

        post("/login") {
            try {
                // Log the content type of the request
                val contentType = call.request.contentType()
                println("Content-Type: $contentType")

                // Ensure content type is application/json
                if (contentType.toString().lowercase() != ContentType.Application.Json.toString().lowercase()) {
                    throw BadRequestException("Content-Type must be application/json")
                }

                // DEBUG: Receive raw text and manually deserialize
                val bodyText = call.receiveText()
                println("RAW BODY: $bodyText")

                val request = Json.decodeFromString<LoginRequest>(bodyText)

                // Validate request using ValidationService
                val validationErrors = ValidationService.validateLogin(
                    email = request.email,
                    password = request.password
                )

                if (validationErrors.isNotEmpty()) {
                    throw ValidationException("Validation failed for login request", validationErrors)
                }

                val user = loginUserUseCase.execute(
                    email = request.email,
                    password = request.password,
                    passwordVerifier = passwordService::verifyPassword
                )

                if (user == null) {
                    throw AuthenticationException("Invalid credentials")
                }

                val accessToken = jwtConfig.createAccessToken(user.id)
                val refreshToken = jwtConfig.createRefreshToken(user.id)

                call.respond(
                    HttpStatusCode.OK,
                    AuthResponse(
                        accessToken = accessToken,
                        refreshToken = refreshToken,
                        userId = user.id.toString()
                    )
                )
            } catch (e: ContentTransformationException) {
                println("Content transformation error: ${e.message}")
                throw BadRequestException("Invalid request format. Please ensure you're sending valid JSON with email and password fields.")
            } catch (e: AuthenticationException) {
                // Let StatusPages handle this
                throw e
            } catch (e: BadRequestException) {
                // Let StatusPages handle this
                throw e
            } catch (e: ValidationException) {
                // Let StatusPages handle this
                throw e
            } catch (e: Exception) {
                println("Login error: ${e.message}")
                e.printStackTrace()
                throw e
            }
        }

        post("/refresh") {
            try {
                // Log the content type of the request
                val contentType = call.request.contentType()
                println("Content-Type for refresh: $contentType")

                // Ensure content type is application/json
                if (contentType.toString().lowercase() != ContentType.Application.Json.toString().lowercase()) {
                    throw BadRequestException("Content-Type must be application/json")
                }

                val request = call.receive<RefreshTokenRequest>()

                // Validate request
                if (request.refreshToken.isBlank()) {
                    throw BadRequestException("Refresh token is required")
                }

                // Verify refresh token
                val userId = jwtConfig.verifyToken(request.refreshToken)
                if (userId == null) {
                    throw AuthenticationException("Invalid refresh token")
                }

                // Get user
                val user = refreshTokenUseCase.execute(userId)
                if (user == null) {
                    throw AuthenticationException("User not found")
                }

                // Generate new tokens
                val accessToken = jwtConfig.createAccessToken(user.id)
                val refreshToken = jwtConfig.createRefreshToken(user.id)

                call.respond(
                    HttpStatusCode.OK, 
                    AuthResponse(
                        accessToken = accessToken,
                        refreshToken = refreshToken,
                        userId = user.id.toString()
                    )
                )
            } catch (e: ContentTransformationException) {
                println("Content transformation error in refresh: ${e.message}")
                throw BadRequestException("Invalid request format. Please ensure you're sending valid JSON with the refreshToken field.")
            } catch (e: AuthenticationException) {
                // Let StatusPages handle this
                throw e
            } catch (e: BadRequestException) {
                // Let StatusPages handle this
                throw e
            } catch (e: Exception) {
                println("Refresh token error: ${e.message}")
                e.printStackTrace()
                throw e
            }
        }

        put("/change-password") {
            try {
                // Log the content type of the request
                val contentType = call.request.contentType()
                println("Content-Type for change-password: $contentType")

                // Ensure content type is application/json
                if (contentType.toString().lowercase() != ContentType.Application.Json.toString().lowercase()) {
                    throw BadRequestException("Content-Type must be application/json")
                }

                val request = call.receive<ChangePasswordRequest>()

                // Basic validation
                if (request.userId.isBlank()) {
                    throw BadRequestException("User ID is required")
                }

                // Validate password using ValidationService
                val validationErrors = ValidationService.validateChangePassword(
                    currentPassword = request.currentPassword,
                    newPassword = request.newPassword
                )

                if (validationErrors.isNotEmpty()) {
                    throw ValidationException("Validation failed for change password request", validationErrors)
                }

                // Parse user ID
                val userId = try {
                    UUID.fromString(request.userId)
                } catch (e: IllegalArgumentException) {
                    throw BadRequestException("Invalid user ID")
                }

                // Verify token from header
                val authHeader = call.request.headers["Authorization"]
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw AuthenticationException("Authentication required")
                }

                val token = authHeader.substring(7)
                val tokenUserId = jwtConfig.verifyToken(token)

                if (tokenUserId == null || tokenUserId != userId) {
                    throw AuthenticationException("Invalid or expired token")
                }

                // Change password
                val success = changePasswordUseCase.execute(
                    userId = userId,
                    currentPassword = request.currentPassword,
                    newPassword = request.newPassword,
                    passwordVerifier = passwordService::verifyPassword,
                    passwordHasher = passwordService::hashPassword
                )

                if (!success) {
                    throw BadRequestException("Failed to change password")
                }

                call.respond(HttpStatusCode.OK, mapOf("message" to "Password changed successfully"))
            } catch (e: ContentTransformationException) {
                println("Content transformation error in change-password: ${e.message}")
                throw BadRequestException("Invalid request format. Please ensure you're sending valid JSON with userId, currentPassword, and newPassword fields.")
            } catch (e: AuthenticationException) {
                // Let StatusPages handle this
                throw e
            } catch (e: BadRequestException) {
                // Let StatusPages handle this
                throw e
            } catch (e: ValidationException) {
                // Let StatusPages handle this
                throw e
            } catch (e: Exception) {
                println("Change password error: ${e.message}")
                e.printStackTrace()
                throw e
            }
        }
    }
}
