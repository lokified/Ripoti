package com.loki.ripoti.presentation.auth.registration

data class RegisterState(
    var firstname: String = "",
    var firstnameError: String? = null,
    var lastname: String = "",
    var lastnameError: String? = null,
    var email: String = "",
    var emailError: String? = null,
    var password: String = "",
    var passwordError: String? = null,
    var conPassword: String = "",
    var conPasswordError: String? = null
)
