package com.loki.ripoti.presentation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.ripoti.domain.model.Profile
import com.loki.ripoti.domain.useCases.user.UserUseCase
import com.loki.ripoti.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): ViewModel() {

    private val _userProfileState = MutableStateFlow(UserProfileState())
    val userProfileState = _userProfileState.asStateFlow()


    fun getUserProfile(profile: Profile) {

        userUseCase.getUserProfile(profile).onEach { result ->

            when(result) {

                is Resource.Loading -> {
                    _userProfileState.value = UserProfileState(
                        isLoading = true
                    )
                }

                is Resource.Success -> {
                    _userProfileState.value = UserProfileState(
                        userProfile = result.data
                    )
                }

                is Resource.Error -> {
                    _userProfileState.value = UserProfileState(
                        error = result.message ?: "Something went wrong"
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}