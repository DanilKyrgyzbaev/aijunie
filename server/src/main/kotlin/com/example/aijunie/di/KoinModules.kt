package com.example.aijunie.di

import com.example.aijunie.data.config.DatabaseConfig
import com.example.aijunie.data.mapper.UserMapper
import com.example.aijunie.data.repository.UserRepositoryImpl
import com.example.aijunie.domain.repository.UserRepository
import com.example.aijunie.domain.usecase.*
import com.example.aijunie.security.JwtConfig
import com.example.aijunie.security.PasswordService
import org.koin.dsl.module

val appModule = module {
    // Configuration
    single { DatabaseConfig }
    single { JwtConfig(System.getenv("JWT_SECRET") ?: "default-secret-change-in-production") }
    single { PasswordService() }
    
    // Repositories
    single<UserRepository> { UserRepositoryImpl() }
    
    // Use Cases
    single { RegisterUserUseCase(get()) }
    single { LoginUserUseCase(get()) }
    single { RefreshTokenUseCase(get()) }
    single { ChangePasswordUseCase(get()) }
    single { GetUserProfileUseCase(get()) }
}