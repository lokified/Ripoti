package com.loki.ripoti.presentation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.ripoti.domain.model.Password
import com.loki.ripoti.domain.model.UserUpdate
import com.loki.ripoti.domain.useCases.user.UserUseCase
import com.loki.ripoti.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class UserUpdateViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): ViewModel(){

    private val _changePassEvent = MutableSharedFlow<UpdateEvent>()
    val changePassEvent = _changePassEvent.asSharedFlow()


    fun updateUser(userId: Int, email: String, user: UserUpdate) {

        userUseCase.updateUser(userId, email, user).onEach { result ->

            when(result) {

                is Resource.Loading -> {
                    _changePassEvent.emit(UpdateEvent.UpdateLoading)
                }

                is Resource.Success -> {
                    _changePassEvent.emit(UpdateEvent.Success(result.data?.message ?: "Something went wrong"))
                }

                is Resource.Error -> {
                    _changePassEvent.emit(UpdateEvent.UpdateError(result.message ?: "Something went wrong"))
                }
            }
        }.launchIn(viewModelScope)
    }


    fun updateUserPassword(userId: Int, email: String, password: Password) {

        userUseCase.updateUserPassword(userId, email, password).onEach { result ->
            when(result) {

                is Resource.Loading -> {
                    _changePassEvent.emit(UpdateEvent.UpdateLoading)
                }

                is Resource.Success -> {
                    _changePassEvent.emit(UpdateEvent.Success(result.data?.message ?: "Something went wrong"))
                }

                is Resource.Error -> {
                    _changePassEvent.emit(UpdateEvent.UpdateError(result.message ?: "Something went wrong"))
                }
            }
        }.launchIn(viewModelScope)
    }


    sealed class UpdateEvent {
        data class Success(val message: String): UpdateEvent()
        object UpdateLoading: UpdateEvent()
        data class UpdateError(val error: String): UpdateEvent()
    }
}