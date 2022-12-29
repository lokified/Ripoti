package com.loki.ripoti.domain.repository

import com.loki.ripoti.data.remote.response.UserProfile
import com.loki.ripoti.data.remote.response.UserResponse
import com.loki.ripoti.domain.model.Password
import com.loki.ripoti.domain.model.Profile
import com.loki.ripoti.domain.model.User
import com.loki.ripoti.domain.model.UserUpdate

interface UserRepository {

    suspend fun insertUser(user: User)

    suspend fun getUser(email: String): User

    suspend fun getAllUsers(): List<User>

    suspend fun getUserProfile(profile: Profile): UserProfile

    suspend fun updateUser(userId: Int, email: String, user: UserUpdate): UserResponse

    suspend fun updatePassword(userId: Int, email: String, password: Password): UserResponse
}