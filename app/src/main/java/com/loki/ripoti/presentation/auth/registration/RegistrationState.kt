package com.loki.ripoti.presentation.auth.registration

data class RegistrationState(
    val error: String = "",
    var message: String = "",
    val isLoading: Boolean = false
)
