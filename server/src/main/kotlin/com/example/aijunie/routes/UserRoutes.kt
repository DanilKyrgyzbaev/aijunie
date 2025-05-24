package com.example.aijunie.routes

import com.example.aijunie.domain.usecase.GetUserProfileUseCase
import com.example.aijunie.exceptions.ResourceNotFoundException
import com.example.aijunie.plugins.UserIdKey
import com.example.aijunie.routes.dto.UserResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.userRoutes(
    getUserProfileUseCase: GetUserProfileUseCase
) {
    route("/api/v1/user") {
        get("/profile") {
            // Get the user ID from the call attributes
            // This is set by the Security plugin if authentication was successful
            val userId = call.attributes[UserIdKey]

            // Get user profile
            val user = getUserProfileUseCase.execute(userId)
            if (user == null) {
                throw ResourceNotFoundException("User not found")
            }

            call.respond(
                HttpStatusCode.OK,
                UserResponse(
                    id = user.id.toString(),
                    name = user.name,
                    lastName = user.lastName,
                    email = user.email
                )
            )
        }
    }
}
