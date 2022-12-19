package com.loki.ripoti.presentation.auth.login

data class LoginState(
    val error: String = "",
    var message: String = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false
)
