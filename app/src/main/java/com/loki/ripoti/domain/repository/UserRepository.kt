package com.loki.ripoti.domain.repository

import com.loki.ripoti.domain.model.User

interface UserRepository {

    suspend fun insertUser(user: User)

    suspend fun getUser(email: String): User

    suspend fun getAllUsers(): List<User>
}