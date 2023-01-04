package com.loki.ripoti.domain.validations

class ValidatePassword {

    fun executeRegister(password: String): ValidationResult {
        if(password.length < 6) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "The password needs to consist of at least 6 characters"
            )
        }
        val containsLettersAndDigits = password.any { it.isDigit() } &&
                password.any { it.isLetter() }
        if(!containsLettersAndDigits) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "The password needs to contain at least one letter and digit"
            )
        }
        return ValidationResult(
            isSuccessful = true
        )
    }

    fun executeLogin(password: String) : ValidationResult {
        if(password.isEmpty()) {
            return ValidationResult(
                isSuccessful = false,
                errorMessage = "Password cannot be blank"
            )
        }

        return ValidationResult(
            isSuccessful = true
        )
    }
}