package com.loki.ripoti.data.repository

import com.loki.ripoti.data.local.UserDao
import com.loki.ripoti.data.remote.RipotiApi
import com.loki.ripoti.data.remote.response.UserResponse
import com.loki.ripoti.domain.model.Password
import com.loki.ripoti.domain.model.User
import com.loki.ripoti.domain.model.UserUpdate
import com.loki.ripoti.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor (
    private val dao: UserDao,
    private val api: RipotiApi
): UserRepository {

    override suspend fun insertUser(user: User) {
        dao.insertUser(user)
    }

    override suspend fun getUser(email: String): User {
        return dao.getUser(email)
    }

    override suspend fun getAllUsers(): List<User> {
        return dao.getAllUsers()
    }

    override suspend fun updateUser(userId: Int, email: String, user: UserUpdate): UserResponse {
        val localUserId = dao.getUserId(email)
        dao.updateUser(user.name, localUserId)
        return api.updateUser(userId, user)
    }

    override suspend fun updatePassword(userId: Int, email: String, password: Password): UserResponse {
        val localUserId = dao.getUserId(email)
        dao.updatePassword(password.newPassword, localUserId)
        return api.updatePassword(userId, password)
    }
}