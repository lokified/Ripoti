package com.loki.ripoti.domain.validations

import android.util.Patterns

class ValidateEmail {
    fun execute(email: String): ValidationResult {
        if(email.isBlank()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "The email can't be blank"
            )
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "That's not a valid email"
            )
        }
        return ValidationResult(
            isSuccessful = true
        )
    }

}