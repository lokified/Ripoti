package com.loki.ripoti.domain.useCases.reports

import com.loki.ripoti.data.remote.response.Reports
import com.loki.ripoti.domain.repository.ReportsRepository
import com.loki.ripoti.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetReportsUseCase(
    private val repository: ReportsRepository
) {

    operator fun invoke(): Flow<Resource<List<Reports>>> = flow {

        try {
            emit(Resource.Loading<List<Reports>>())
            val reports = repository.getReports()
            emit(Resource.Success<List<Reports>>(data = reports))
        } catch (e: HttpException) {
            emit(
                Resource.Error<List<Reports>>(
                    e.localizedMessage ?: "An unexpected error occurred"
                )
            )
        } catch (e: IOException) {
            emit(Resource.Error<List<Reports>>("check your internet connection"))
        }
    }
}