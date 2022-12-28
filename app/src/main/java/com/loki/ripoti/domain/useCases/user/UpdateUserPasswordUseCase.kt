package com.loki.ripoti.domain.useCases.user

import com.loki.ripoti.data.remote.response.UserResponse
import com.loki.ripoti.domain.model.Password
import com.loki.ripoti.domain.model.User
import com.loki.ripoti.domain.repository.UserRepository
import com.loki.ripoti.util.Resource
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class UpdateUserPasswordUseCase(
    private val repository: UserRepository
) {

     operator fun invoke(userId: Int, email: String, password: Password) = flow<Resource<UserResponse>> {
        try {
            emit(Resource.Loading<UserResponse>(data = null))
            emit(Resource.Success<UserResponse>(data = repository.updatePassword(userId, email, password)))
        }
        catch (e: HttpException) {
            emit(Resource.Error<UserResponse>(e.localizedMessage ?: "An unexpected error occurred", data = null))
        }
        catch (e: IOException) {
            emit(Resource.Error<UserResponse>("check your internet connection", data = null))
        }
    }
}