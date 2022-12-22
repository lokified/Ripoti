package com.loki.ripoti.data.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reports(
    val id: Int,
    val username: String,
    val name: String,
    val description: String,
    val created_at: String,
    val created_on: String
): Parcelable