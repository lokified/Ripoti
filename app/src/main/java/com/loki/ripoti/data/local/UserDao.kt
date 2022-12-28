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

    @Query("SELECT * FROM User WHERE email = :email")
    suspend fun getUser(email: String): User

    @Query("SELECT id FROM User WHERE email = :email")
    suspend fun getUserId(email: String): Int

    @Query("SELECT * FROM User")
    suspend fun getAllUsers(): List<User>

    @Query("UPDATE User SET password = :password WHERE id = :userId")
    suspend fun updatePassword(password: String, userId: Int)

    @Query("UPDATE User SET name = :name WHERE id = :userId")
    suspend fun updateUser(name: String, userId: Int)
}