package com.example.aijunie.plugins

import com.example.aijunie.exceptions.*
import com.example.aijunie.routes.dto.ApiError
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.path
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        // Handle specific API exceptions
        exception<AuthenticationException> { call, cause ->
            call.response.header("Content-Type", "application/problem+json")
            call.respond(
                HttpStatusCode.Unauthorized,
                ApiError(
                    type = "https://example.com/probs/authentication",
                    title = "Authentication Failed",
                    status = HttpStatusCode.Unauthorized.value,
                    detail = cause.message ?: "Invalid credentials.",
                    instance = call.request.path()
                )
            )
        }

        exception<ResourceNotFoundException> { call, cause ->
            call.response.header("Content-Type", "application/problem+json")
            call.respond(
                HttpStatusCode.NotFound,
                ApiError(
                    type = "https://example.com/probs/not-found",
                    title = "Resource Not Found",
                    status = HttpStatusCode.NotFound.value,
                    detail = cause.message ?: "The requested resource was not found.",
                    instance = call.request.path()
                )
            )
        }

        exception<ValidationException> { call, cause ->
            call.response.header("Content-Type", "application/problem+json")
            call.respond(
                HttpStatusCode.UnprocessableEntity,
                ApiError(
                    type = "https://example.com/probs/validation",
                    title = "Validation Failed",
                    status = HttpStatusCode.UnprocessableEntity.value,
                    detail = cause.message ?: "The request contains invalid data.",
                    instance = call.request.path(),
                    errors = cause.errors
                )
            )
        }

        exception<BadRequestException> { call, cause ->
            call.response.header("Content-Type", "application/problem+json")
            call.respond(
                HttpStatusCode.BadRequest,
                ApiError(
                    type = "https://example.com/probs/bad-request",
                    title = "Bad Request",
                    status = HttpStatusCode.BadRequest.value,
                    detail = cause.message ?: "The request was invalid.",
                    instance = call.request.path()
                )
            )
        }

        exception<ForbiddenException> { call, cause ->
            call.response.header("Content-Type", "application/problem+json")
            call.respond(
                HttpStatusCode.Forbidden,
                ApiError(
                    type = "https://example.com/probs/forbidden",
                    title = "Access Forbidden",
                    status = HttpStatusCode.Forbidden.value,
                    detail = cause.message ?: "You don't have permission to access this resource.",
                    instance = call.request.path()
                )
            )
        }

        // Catch-all for other API exceptions
        exception<ApiException> { call, cause ->
            call.response.header("Content-Type", "application/problem+json")
            call.respond(
                HttpStatusCode.InternalServerError,
                ApiError(
                    type = "https://example.com/probs/api-error",
                    title = "API Error",
                    status = HttpStatusCode.InternalServerError.value,
                    detail = cause.message ?: "An unexpected API error occurred.",
                    instance = call.request.path()
                )
            )
        }

        // Catch-all for any other exceptions
        exception<Throwable> { call, cause ->
            call.response.header("Content-Type", "application/problem+json")
            call.respond(
                HttpStatusCode.InternalServerError,
                ApiError(
                    type = "https://example.com/probs/internal",
                    title = "Internal Server Error",
                    status = HttpStatusCode.InternalServerError.value,
                    detail = cause.message ?: "An unexpected error occurred.",
                    instance = call.request.path()
                )
            )
        }
    }
}
