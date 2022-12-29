package com.loki.ripoti.domain.useCases.user

import com.loki.ripoti.data.remote.response.UserProfile
import com.loki.ripoti.domain.model.Profile
import com.loki.ripoti.domain.repository.UserRepository
import com.loki.ripoti.util.Resource
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetUserProfileUseCase(
    private val repository: UserRepository
) {

    operator fun invoke(profile: Profile) = flow<Resource<UserProfile>> {

        try {
            emit(Resource.Loading<UserProfile>(data = null))
            emit(Resource.Success<UserProfile>(data = repository.getUserProfile(profile)))
        }
        catch (e: HttpException) {
            emit(Resource.Error<UserProfile>(e.localizedMessage ?: "An unexpected error occurred", data = null))
        }
        catch (e: IOException) {
            emit(Resource.Error<UserProfile>("check your internet connection", data = null))
        }
    }
}