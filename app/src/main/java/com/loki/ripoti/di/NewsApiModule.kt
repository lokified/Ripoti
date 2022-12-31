package com.loki.ripoti.di

import com.loki.ripoti.data.remote.NewsApi
import com.loki.ripoti.data.repository.NewsRepositoryImpl
import com.loki.ripoti.domain.repository.NewsRepository
import com.loki.ripoti.domain.useCases.news.GetNewsUseCase
import com.loki.ripoti.domain.useCases.news.NewsUseCase
import com.loki.ripoti.util.Constants.NEWS_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsApiModule {

    @Provides
    @Singleton
    fun provideNewsApi() : NewsApi {
        return Retrofit.Builder()
            .baseUrl(NEWS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }

    @Singleton
    @Provides
    fun provideNewsRepository(newsApi: NewsApi): NewsRepository {
        return NewsRepositoryImpl(newsApi)
    }

    @Singleton
    @Provides
    fun provideNewsUseCase(repository: NewsRepository): NewsUseCase {
        return NewsUseCase(
            getNews = GetNewsUseCase(repository)
        )
    }
}