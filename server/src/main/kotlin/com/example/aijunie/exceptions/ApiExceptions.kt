package com.example.aijunie.exceptions

/**
 * Base class for all API exceptions
 */
open class ApiException(message: String) : Exception(message)

/**
 * Exception thrown when authentication fails
 */
class AuthenticationException(message: String = "Authentication failed") : ApiException(message)

/**
 * Exception thrown when a resource is not found
 */
class ResourceNotFoundException(message: String = "Resource not found") : ApiException(message)

/**
 * Exception thrown when a request is invalid
 */
class BadRequestException(message: String = "Bad request") : ApiException(message)

/**
 * Exception thrown when a user is not authorized to access a resource
 */
class ForbiddenException(message: String = "Access forbidden") : ApiException(message)

/**
 * Exception thrown when validation fails
 * @param message Error message
 * @param errors Map of field names to validation error messages
 */
class ValidationException(
    message: String = "Validation failed",
    val errors: Map<String, List<String>> = emptyMap()
) : ApiException(message)
