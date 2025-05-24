package com.example.aijunie.routes.dto

import kotlinx.serialization.Serializable

/**
 * Represents an API error response following the RFC 7807 Problem Details for HTTP APIs specification.
 *
 * @property type A URI reference that identifies the problem type
 * @property title A short, human-readable summary of the problem type
 * @property status The HTTP status code
 * @property detail A human-readable explanation specific to this occurrence of the problem
 * @property instance A URI reference that identifies the specific occurrence of the problem
 * @property errors Optional map of field names to validation error messages
 */
@Serializable
data class ApiError(
    val type: String,
    val title: String,
    val status: Int,
    val detail: String,
    val instance: String,
    val errors: Map<String, List<String>>? = null
)
