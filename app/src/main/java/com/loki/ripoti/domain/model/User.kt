package com.loki.ripoti.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val id: Int? = null,
    val name: String,
    val email: String,
    val password: String
)
