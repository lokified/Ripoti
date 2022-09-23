package com.loki.ripoti.ui.onboarding.registration

data class RegistrationState(
    val error: String = "",
    var message: String = "",
    val isLoading: Boolean = false
)
