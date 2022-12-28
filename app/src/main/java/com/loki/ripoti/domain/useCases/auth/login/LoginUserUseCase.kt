package com.loki.ripoti.domain.useCases.auth.login

import com.loki.ripoti.data.remote.response.LoginResponse
import com.loki.ripoti.domain.model.Login
import com.loki.ripoti.domain.repository.AuthRepository
import com.loki.ripoti.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class LoginUserUseCase (
    private val repository: AuthRepository
) {

    operator fun invoke(login: Login): Flow<Resource<LoginResponse>> = flow{

        try {
            emit(Resource.Loading<LoginResponse>())
            emit(Resource.Success<LoginResponse>(data = repository.loginUser(login)))
        }
        catch (e: HttpException) {
            emit(Resource.Error<LoginResponse>(e.localizedMessage ?: "An unexpected error occurred", data = null))
        }
        catch (e: IOException) {
            emit(Resource.Error<LoginResponse>("check your internet connection", data = null))
        }
    }
}