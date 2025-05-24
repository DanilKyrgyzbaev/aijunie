package com.example.aijunie.data.mapper

import com.example.aijunie.data.model.UserEntity
import com.example.aijunie.domain.model.User

object UserMapper {
    fun toDomain(entity: UserEntity): User {
        return User(
            id = entity.id.value,
            name = entity.name,
            lastName = entity.lastName,
            email = entity.email,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
    
    fun toEntity(domain: User, passwordHash: String? = null): UserEntity {
        return UserEntity.new(domain.id) {
            name = domain.name
            lastName = domain.lastName
            email = domain.email
            if (passwordHash != null) {
                this.passwordHash = passwordHash
            }
            createdAt = domain.createdAt
            updatedAt = domain.updatedAt
        }
    }
}