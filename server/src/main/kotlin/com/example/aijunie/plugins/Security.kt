package com.example.aijunie.plugins

import com.example.aijunie.security.JwtConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import org.koin.ktor.ext.inject
import java.util.UUID

// Custom authentication attribute key
val UserIdKey = AttributeKey<UUID>("userId")

fun Application.configureSecurity() {
    val jwtConfig: JwtConfig by inject()

    // Create a route interceptor for JWT authentication
    intercept(ApplicationCallPipeline.Plugins) {
        // Skip authentication for auth routes
        val path = call.request.path()
        if (path.startsWith("/api/v1/auth") || path == "/") {
            return@intercept
        }

        try {
            // Extract token from Authorization header
            val authHeader = call.request.header("Authorization")
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("message" to "Missing or invalid Authorization header"))
                return@intercept finish()
            }

            val token = authHeader.substring(7)
            val userId = jwtConfig.verifyToken(token)

            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("message" to "Invalid or expired token"))
                return@intercept finish()
            }

            // Store the user ID in the call attributes for later use
            call.attributes.put(UserIdKey, userId)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Unauthorized, mapOf("message" to "Authentication failed"))
            return@intercept finish()
        }
    }
}
