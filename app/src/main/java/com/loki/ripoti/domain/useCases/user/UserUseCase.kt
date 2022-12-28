package com.loki.ripoti.domain.useCases.user

data class UserUseCase (
    val updateUser: UpdateUserUseCase,
    val updateUserPassword: UpdateUserPasswordUseCase
    )