package com.loki.ripoti.domain.validations

data class ValidationResult(
    val errorMessage: String = "",
    val isSuccessful: Boolean = false
)
