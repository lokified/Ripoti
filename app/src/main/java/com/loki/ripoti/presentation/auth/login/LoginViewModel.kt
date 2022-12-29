package com.loki.ripoti.presentation.auth.login

import androidx.lifecycle.*
import com.loki.ripoti.domain.model.Login
import com.loki.ripoti.domain.model.Profile
import com.loki.ripoti.domain.model.User
import com.loki.ripoti.domain.repository.UserRepository
import com.loki.ripoti.domain.useCases.auth.AuthUseCase
import com.loki.ripoti.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val userRepository: UserRepository,
): ViewModel() {

    private val _loginEvent = MutableSharedFlow<LoginEvent>()
    val loginEvent = _loginEvent.asSharedFlow()

    fun loginUser(login: Login) {

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


    sealed class LoginEvent {

        object LoginLoading: LoginEvent()
        data class LoginError(val error: String): LoginEvent()
        data class Success(val token: String): LoginEvent()
    }

}