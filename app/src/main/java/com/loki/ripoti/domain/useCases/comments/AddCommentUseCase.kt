package com.loki.ripoti.domain.useCases.comments

import com.loki.ripoti.data.remote.response.UserResponse
import com.loki.ripoti.domain.model.Comment
import com.loki.ripoti.domain.model.Report
import com.loki.ripoti.domain.repository.CommentsRepository
import com.loki.ripoti.domain.repository.ReportsRepository
import com.loki.ripoti.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class AddCommentUseCase(
    private val repository: CommentsRepository
) {

    operator fun invoke(comment: Comment): Flow<Resource<UserResponse>> = flow {

        try {
            emit(Resource.Loading<UserResponse>(data = null))
            emit(Resource.Success<UserResponse>(data = repository.addComment(comment)))
        }
        catch (e: HttpException) {
            emit(Resource.Error<UserResponse>(e.localizedMessage ?: "An unexpected error occurred", data = null))
        }
        catch (e: IOException) {
            emit(Resource.Error<UserResponse>("check your internet connection", data = null))
        }
    }
}