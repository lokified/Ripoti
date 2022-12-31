package com.loki.ripoti.data.remote

import com.loki.ripoti.data.remote.response.NewsItem
import retrofit2.http.GET

interface NewsApi {

    @GET("accident-news")
    suspend fun getNewsList(): List<NewsItem>
}