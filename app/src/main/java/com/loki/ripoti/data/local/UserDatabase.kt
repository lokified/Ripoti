package com.loki.ripoti.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.loki.ripoti.domain.model.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
abstract class UserDatabase: RoomDatabase() {

    abstract val userDao: UserDao

    companion object {
        const val DATABASE_NAME = "user_db"
    }
}