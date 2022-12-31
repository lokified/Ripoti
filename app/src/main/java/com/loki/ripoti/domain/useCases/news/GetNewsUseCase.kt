package com.loki.ripoti.domain.useCases.news

import com.loki.ripoti.data.remote.response.NewsItem
import com.loki.ripoti.data.remote.response.Reports
import com.loki.ripoti.domain.repository.NewsRepository
import com.loki.ripoti.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetNewsUseCase (
    private val newsRepository: NewsRepository
    ) {

    operator fun invoke(): Flow<Resource<List<NewsItem>>> = flow {

        try {
            emit(Resource.Loading<List<NewsItem>>())
            val news = newsRepository.getNews()
            emit(Resource.Success<List<NewsItem>>(data = news))
        } catch (e: HttpException) {
            emit(
                Resource.Error<List<NewsItem>>(
                    e.localizedMessage ?: "An unexpected error occurred"
                )
            )
        } catch (e: IOException) {
            emit(Resource.Error<List<NewsItem>>("check your internet connection"))
        }
    }
}