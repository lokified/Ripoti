package com.loki.ripoti.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.ripoti.domain.model.User
import com.loki.ripoti.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _localUsers = MutableStateFlow<List<User>>(emptyList())
    val localUsers = _localUsers.asStateFlow()

    init {
        getLocalUser()
    }

    private fun getLocalUser() {

        viewModelScope.launch {
            _localUsers.value = userRepository.getAllUsers()
        }
    }
}