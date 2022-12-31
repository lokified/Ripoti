package com.loki.ripoti.domain.repository

import com.loki.ripoti.data.remote.response.NewsItem

interface NewsRepository {

    suspend fun getNews(): List<NewsItem>
}