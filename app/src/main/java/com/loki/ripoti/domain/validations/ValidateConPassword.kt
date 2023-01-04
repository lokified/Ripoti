package com.loki.ripoti.domain.validations

class ValidateConPassword {

    fun execute(password: String, conPassword: String): ValidationResult {
        if(password != conPassword) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "The passwords don't match"
            )
        }
        return ValidationResult(
            isSuccessful = true
        )
    }
}