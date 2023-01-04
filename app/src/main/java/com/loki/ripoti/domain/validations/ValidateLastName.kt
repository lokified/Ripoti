package com.loki.ripoti.domain.validations

class ValidateLastName {

    fun execute(lastname: String): ValidationResult {
        if (lastname.isBlank()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Lastname cannot be blank"
            )
        }

        return ValidationResult(
            isSuccessful = true
        )
    }
}