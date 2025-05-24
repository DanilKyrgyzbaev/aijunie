package com.example.aijunie.plugins

import com.example.aijunie.domain.usecase.*
import com.example.aijunie.routes.authRoutes
import com.example.aijunie.routes.userRoutes
import com.example.aijunie.security.JwtConfig
import com.example.aijunie.security.PasswordService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val registerUserUseCase: RegisterUserUseCase by inject()
    val loginUserUseCase: LoginUserUseCase by inject()
    val refreshTokenUseCase: RefreshTokenUseCase by inject()
    val changePasswordUseCase: ChangePasswordUseCase by inject()
    val getUserProfileUseCase: GetUserProfileUseCase by inject()
    val jwtConfig: JwtConfig by inject()
    val passwordService: PasswordService by inject()

    routing {
        authRoutes(
            registerUserUseCase = registerUserUseCase,
            loginUserUseCase = loginUserUseCase,
            refreshTokenUseCase = refreshTokenUseCase,
            changePasswordUseCase = changePasswordUseCase,
            jwtConfig = jwtConfig,
            passwordService = passwordService
        )

        userRoutes(
            getUserProfileUseCase = getUserProfileUseCase
        )
    }
}
