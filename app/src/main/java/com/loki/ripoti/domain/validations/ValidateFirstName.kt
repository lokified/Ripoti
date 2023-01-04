package com.loki.ripoti.domain.validations

class ValidateFirstName {

    fun execute(firstname: String): ValidationResult {
        if (firstname.isBlank()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Firstname cannot be blank"
            )
        }

        return ValidationResult(
            isSuccessful = true
        )
    }
}