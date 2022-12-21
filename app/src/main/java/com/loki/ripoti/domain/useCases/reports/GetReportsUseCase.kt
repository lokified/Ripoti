package com.loki.ripoti.domain.useCases.reports

import com.loki.ripoti.data.remote.response.Reports
import com.loki.ripoti.data.remote.response.UserReports
import com.loki.ripoti.domain.repository.ReportsRepository
import com.loki.ripoti.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetReportsUseCase(
    private val repository: ReportsRepository
) {

    operator fun invoke(): Flow<Resource<List<UserReports>>> = flow {

        try {
            emit(Resource.Loading<List<UserReports>>())
            val reports = repository.getReports()
            emit(Resource.Success<List<UserReports>>(data = reports))
        } catch (e: HttpException) {
            emit(
                Resource.Error<List<UserReports>>(
                    e.localizedMessage ?: "An unexpected error occurred"
                )
            )
        } catch (e: IOException) {
            emit(Resource.Error<List<UserReports>>("check your internet connection"))
        }
    }
}