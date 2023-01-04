package com.loki.ripoti.presentation.auth.login.views

data class LoginState(
    var email: String = "",
    var emailError: String? = null,
    var password: String = "",
    var passwordError: String? = null
)
