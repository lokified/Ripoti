package com.loki.ripoti.domain.useCases.auth

import com.loki.ripoti.domain.useCases.auth.login.LoginUserUseCase
import com.loki.ripoti.domain.useCases.auth.registration.RegisterUserUseCase

data class AuthUseCase(
    val loginUser: LoginUserUseCase,
    val registerUser: RegisterUserUseCase
)
