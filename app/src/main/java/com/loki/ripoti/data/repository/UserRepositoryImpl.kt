package com.loki.ripoti.data.repository

import com.loki.ripoti.data.local.UserDao
import com.loki.ripoti.domain.model.User
import com.loki.ripoti.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl (
    private val dao: UserDao
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
}