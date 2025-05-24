package com.example.aijunie.domain.model

import kotlinx.datetime.LocalDateTime
import java.util.UUID

data class User(
    val id: UUID,
    val name: String,
    val lastName: String,
    val email: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
