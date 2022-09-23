package com.loki.ripoti.domain.useCases.comments

import com.loki.ripoti.data.remote.response.Comments
import com.loki.ripoti.domain.repository.CommentsRepository
import com.loki.ripoti.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetCommentsUseCase(
    private val repository: CommentsRepository
) {

    operator fun invoke(reportId: Int): Flow<Resource<List<Comments>>> = flow {

        try {
            emit(Resource.Loading<List<Comments>>())
            val comments = repository.getCommentsInReport(reportId)
            emit(Resource.Success<List<Comments>>(data = comments))
        } catch (e: HttpException) {
            emit(
                Resource.Error<List<Comments>>(
                    e.localizedMessage ?: "An unexpected error occurred"
                )
            )
        } catch (e: IOException) {
            emit(Resource.Error<List<Comments>>("check your internet connection"))
        }
    }
}