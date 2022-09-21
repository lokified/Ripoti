package com.loki.ripoti.data.repository

import com.loki.ripoti.data.remote.RipotiApi
import com.loki.ripoti.data.remote.response.LoginResponse
import com.loki.ripoti.data.remote.response.UserResponse
import com.loki.ripoti.domain.model.Login
import com.loki.ripoti.domain.model.User
import com.loki.ripoti.domain.repository.OnBoardingRepository
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val api: RipotiApi
): OnBoardingRepository {

    override suspend fun registerUser(user: User): UserResponse {
        return api.registerUser(user)
    }

    override suspend fun loginUser(login: Login): LoginResponse {
        return api.loginUser(login)
    }
}