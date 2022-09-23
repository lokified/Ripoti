package com.loki.ripoti.ui.onboarding.login

import androidx.lifecycle.*
import com.loki.ripoti.domain.model.Login
import com.loki.ripoti.domain.model.User
import com.loki.ripoti.domain.useCases.auth.AuthUseCase
import com.loki.ripoti.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
): ViewModel() {

    private val _state = MutableLiveData(LoginState())
    val state: LiveData<LoginState> = _state

    fun loginUser(login: Login) {

        authUseCase.loginUser(login).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _state.value = LoginState(
                        isLoading = true
                    )
                }
                is Resource.Success -> {
                    _state.value = LoginState(
                        message = result.data?.token!!,
                        isLoggedIn = true
                    )
                }
                is Resource.Error -> {
                    _state.value = LoginState(
                        error = result.message!!
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

}