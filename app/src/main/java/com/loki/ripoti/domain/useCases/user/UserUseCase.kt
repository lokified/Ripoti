package com.loki.ripoti.domain.useCases.user

data class UserUseCase (
    val getUserProfile: GetUserProfileUseCase,
    val updateUser: UpdateUserUseCase,
    val updateUserPassword: UpdateUserPasswordUseCase
    )