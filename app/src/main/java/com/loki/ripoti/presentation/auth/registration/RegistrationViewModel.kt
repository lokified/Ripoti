package com.loki.ripoti.presentation.auth.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.ripoti.domain.model.User
import com.loki.ripoti.domain.repository.UserRepository
import com.loki.ripoti.domain.useCases.auth.AuthUseCase
import com.loki.ripoti.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val userRepository: UserRepository
): ViewModel() {

    private val _registerState = MutableSharedFlow<RegisterEvent>()
    val registerState = _registerState.asSharedFlow()

    fun registerUser(user: User) {

        authUseCase.registerUser(user).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _registerState.emit(RegisterEvent.RegisterLoading)
                }
                is Resource.Success -> {
                    userRepository.insertUser(user)
                    _registerState.emit(RegisterEvent.Success(result.data?.message ?: "Something went wrong"))
                }
                is Resource.Error -> {
                    _registerState.emit(RegisterEvent.RegisterError(result.message ?: "Something went wrong"))
                }
            }
        }.launchIn(viewModelScope)
    }


    sealed class RegisterEvent {
        data class Success(val message: String): RegisterEvent()
        data class RegisterError(val error: String): RegisterEvent()
        object RegisterLoading: RegisterEvent()
    }
}