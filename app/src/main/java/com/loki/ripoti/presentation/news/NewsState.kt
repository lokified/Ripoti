package com.loki.ripoti.presentation.news

import com.loki.ripoti.data.remote.response.NewsItem

data class NewsState(
    val isLoading: Boolean = false,
    val newsList: List<NewsItem> = emptyList(),
    val error: String = ""
)
