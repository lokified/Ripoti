package com.loki.ripoti.ui.onboarding.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.ripoti.domain.model.User
import com.loki.ripoti.domain.useCases.auth.AuthUseCase
import com.loki.ripoti.ui.onboarding.login.LoginState
import com.loki.ripoti.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
): ViewModel() {

    private val _state = MutableLiveData(RegistrationState())
    val state: LiveData<RegistrationState> = _state

    fun registerUser(user: User) {

        authUseCase.registerUser(user).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _state.value = RegistrationState(
                        isLoading = true
                    )
                }
                is Resource.Success -> {
                    _state.value = RegistrationState(
                        message = result.data?.message!!
                    )
                }
                is Resource.Error -> {
                    _state.value = RegistrationState(
                        error = result.message!!
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}