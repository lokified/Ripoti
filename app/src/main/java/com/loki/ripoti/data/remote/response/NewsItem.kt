package com.loki.ripoti.data.remote.response

data class NewsItem(
    val title: String,
    val brief_description: String,
    val image_url: String,
    val news_url: String
)