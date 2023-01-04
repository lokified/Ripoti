package com.loki.ripoti.presentation.auth.login

import androidx.lifecycle.*
import com.loki.ripoti.domain.model.Login
import com.loki.ripoti.domain.model.Profile
import com.loki.ripoti.domain.model.User
import com.loki.ripoti.domain.repository.UserRepository
import com.loki.ripoti.domain.useCases.auth.AuthUseCase
import com.loki.ripoti.domain.validations.ValidateEmail
import com.loki.ripoti.domain.validations.ValidatePassword
import com.loki.ripoti.presentation.auth.login.views.LoginState
import com.loki.ripoti.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val userRepository: UserRepository,
): ViewModel() {

    private val _loginEvent = MutableSharedFlow<LoginEvent>()
    val loginEvent = _loginEvent.asSharedFlow()

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val validateEmail : ValidateEmail = ValidateEmail()
    private val validatePassword: ValidatePassword = ValidatePassword()


    fun onEvent(event: LoginFormEvent) {

        when(event) {
            is LoginFormEvent.EmailChanged -> {
                _loginState.value = _loginState.value.copy(
                    email = event.email
                )
            }

            is LoginFormEvent.PasswordChanged -> {
                _loginState.value = _loginState.value.copy(
                    password = event.password
                )
            }

            is LoginFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {

        val emailResult = validateEmail.execute(_loginState.value.email)
        val passwordResult = validatePassword.executeLogin(_loginState.value.password)

        val hasError = listOf(
            emailResult,
            passwordResult
        ).any { !it.isSuccessful }

        if (hasError) {
            _loginState.value = _loginState.value.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage
            )
            return
        }

        _loginState.value = _loginState.value.copy(
            emailError = null,
            passwordError = null
        )

        val login = Login(
            email = _loginState.value.email,
            password = _loginState.value.password
        )

        authUseCase.loginUser(login).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _loginEvent.emit(LoginEvent.LoginLoading)
                }
                is Resource.Success -> {
                    val profile = Profile(login.email)
                    val userProfile = userRepository.getUserProfile(profile)
                    val user = User(
                        id = 0,
                        name = userProfile.name,
                        email = userProfile.email,
                        password = login.password
                    )
                    userRepository.insertUser(user)
                    _loginEvent.emit(LoginEvent.Success(result.data?.token!!))
                }
                is Resource.Error -> {
                    _loginEvent.emit(LoginEvent.LoginError(result.message ?: "Something went wrong"))
                }
            }
        }.launchIn(viewModelScope)
    }


    sealed class LoginFormEvent {
        data class EmailChanged(val email: String): LoginFormEvent()
        data class PasswordChanged(val password: String): LoginFormEvent()
        object Submit: LoginFormEvent()
    }

    sealed class LoginEvent {

        object LoginLoading: LoginEvent()
        data class LoginError(val error: String): LoginEvent()
        data class Success(val token: String): LoginEvent()
    }

}