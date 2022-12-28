package com.loki.ripoti.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Entity
data class User(
    @PrimaryKey val id: Int? = null,
    val name: String? = null,
    val email: String? = null,
    val password: String? = null
) {

//    companion object {
//
//        val MIGRATION_2_3 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("""
//                CREATE TABLE users (
//                    id INTEGER PRIMARY KEY,
//                    name TEXT,
//                    email TEXT,
//                    password TEXT
//                )
//                """.trimIndent())
//                database.execSQL("DROP TABLE User")
//                database.execSQL("ALTER TABLE users RENAME TO User")
//            }
//        }
//    }
}
