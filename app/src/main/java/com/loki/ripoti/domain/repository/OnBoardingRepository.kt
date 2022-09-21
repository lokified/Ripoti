package com.loki.ripoti.domain.repository

import com.loki.ripoti.data.remote.response.LoginResponse
import com.loki.ripoti.data.remote.response.UserResponse
import com.loki.ripoti.domain.model.Login
import com.loki.ripoti.domain.model.User

interface OnBoardingRepository {

    suspend fun registerUser(user: User): UserResponse
    suspend fun loginUser(login: Login): LoginResponse
}