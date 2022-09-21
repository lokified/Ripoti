package com.loki.ripoti.ui.onboarding.login

data class LoginState(
    val error: String = "",
    val message: String = "",
    val isLoading: Boolean = false
)
