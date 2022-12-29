package com.loki.ripoti.presentation.account

import com.loki.ripoti.data.remote.response.UserProfile

data class UserProfileState(
    val isLoading: Boolean = false,
    val userProfile: UserProfile? = null,
    val error: String = ""
)
