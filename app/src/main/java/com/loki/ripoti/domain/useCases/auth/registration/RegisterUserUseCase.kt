package com.loki.ripoti.domain.useCases.auth.registration

import com.loki.ripoti.data.remote.response.UserResponse
import com.loki.ripoti.domain.model.User
import com.loki.ripoti.domain.repository.OnBoardingRepository
import com.loki.ripoti.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class RegisterUserUseCase (
    private val repository: OnBoardingRepository
) {

    operator fun invoke(user: User): Flow<Resource<UserResponse>> = flow{

        try {
            emit(Resource.Loading<UserResponse>(data = null))
            emit(Resource.Success<UserResponse>(data = repository.registerUser(user)))
        }
        catch (e: HttpException) {
            emit(Resource.Error<UserResponse>(e.localizedMessage ?: "An unexpected error occurred", data = null))
        }
        catch (e: IOException) {
            emit(Resource.Error<UserResponse>("check your internet connection", data = null))
        }
    }
}