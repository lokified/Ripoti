package com.loki.ripoti.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.loki.ripoti.domain.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user WHERE email = :email")
    suspend fun getUser(email: String): User

    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<User>
}