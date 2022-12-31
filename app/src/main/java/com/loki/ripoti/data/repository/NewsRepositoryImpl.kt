package com.loki.ripoti.data.repository

import com.loki.ripoti.data.remote.NewsApi
import com.loki.ripoti.data.remote.response.NewsItem
import com.loki.ripoti.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi
): NewsRepository {

    override suspend fun getNews(): List<NewsItem> {
        return newsApi.getNewsList()
    }
}