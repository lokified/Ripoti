package com.loki.ripoti.presentation.auth.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.ripoti.domain.model.User
import com.loki.ripoti.domain.repository.UserRepository
import com.loki.ripoti.domain.useCases.auth.AuthUseCase
import com.loki.ripoti.domain.validations.*
import com.loki.ripoti.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val userRepository: UserRepository
): ViewModel() {

    private val _registerEvent = MutableSharedFlow<RegisterEvent>()
    val registerEvent = _registerEvent.asSharedFlow()

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

    private val validateFirstName: ValidateFirstName = ValidateFirstName()
    private val validateLastName: ValidateLastName = ValidateLastName()
    private val validateEmail: ValidateEmail = ValidateEmail()
    private val validatePassword: ValidatePassword = ValidatePassword()
    private val validateConPassword: ValidateConPassword = ValidateConPassword()


    fun onEvent(event: RegistrationFormEvent) {

        when(event) {
            is RegistrationFormEvent.FirstNameChanged -> {
                _registerState.value = registerState.value.copy(
                    firstname = event.firstname
                )
            }

            is RegistrationFormEvent.LastNameChanged -> {
                _registerState.value = registerState.value.copy(
                    lastname = event.lastname
                )
            }

            is RegistrationFormEvent.EmailChanged -> {
                _registerState.value = registerState.value.copy(
                    email = event.email
                )
            }

            is RegistrationFormEvent.PasswordChanged -> {
                _registerState.value = registerState.value.copy(
                    password = event.password
                )
            }

            is RegistrationFormEvent.ConPasswordChanged -> {
                _registerState.value = registerState.value.copy(
                    conPassword = event.conPassword
                )
            }

            is RegistrationFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {

        val firstnameResult = validateFirstName.execute(_registerState.value.firstname)
        val lastnameResult = validateLastName.execute(_registerState.value.lastname)
        val emailResult = validateEmail.execute(_registerState.value.email)
        val passwordResult = validatePassword.executeRegister(_registerState.value.password)
        val conPasswordResult = validateConPassword.execute(_registerState.value.password, _registerState.value.conPassword)


        val hasError = listOf(
            firstnameResult,
            lastnameResult,
            emailResult,
            passwordResult,
            conPasswordResult
        ).any { !it.isSuccessful }

        if (hasError) {

            _registerState.value = _registerState.value.copy(
                firstnameError = firstnameResult.errorMessage,
                lastnameError = lastnameResult.errorMessage,
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                conPasswordError = conPasswordResult.errorMessage
            )
            return
        }

        _registerState.value = _registerState.value.copy(
            firstnameError = null,
            lastnameError = null,
            emailError = null,
            passwordError = null,
            conPasswordError = null
        )

        val user = User(
            id = 0,
            name = "${_registerState.value.firstname} ${_registerState.value.lastname}",
            email = _registerState.value.email,
            password = _registerState.value.password
        )

        authUseCase.registerUser(user).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _registerEvent.emit(RegisterEvent.RegisterLoading)
                }
                is Resource.Success -> {
                    userRepository.insertUser(user)
                    _registerEvent.emit(RegisterEvent.Success(result.data?.message ?: "Something went wrong"))
                }
                is Resource.Error -> {
                    _registerEvent.emit(RegisterEvent.RegisterError(result.message ?: "Something went wrong"))
                }
            }
        }.launchIn(viewModelScope)
    }

    sealed class RegistrationFormEvent {
        data class FirstNameChanged(val firstname: String): RegistrationFormEvent()
        data class LastNameChanged(val lastname: String): RegistrationFormEvent()
        data class EmailChanged(val email: String): RegistrationFormEvent()
        data class PasswordChanged(val password: String): RegistrationFormEvent()
        data class ConPasswordChanged(val conPassword: String): RegistrationFormEvent()
        object Submit: RegistrationFormEvent()
    }

    sealed class RegisterEvent {
        data class Success(val message: String): RegisterEvent()
        data class RegisterError(val error: String): RegisterEvent()
        object RegisterLoading: RegisterEvent()
    }
}