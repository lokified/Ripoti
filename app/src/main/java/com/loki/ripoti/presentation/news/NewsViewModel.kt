package com.loki.ripoti.presentation.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.ripoti.data.remote.response.NewsItem
import com.loki.ripoti.domain.repository.NewsRepository
import com.loki.ripoti.domain.useCases.news.NewsUseCase
import com.loki.ripoti.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsUseCase: NewsUseCase
): ViewModel() {

    private val _newsState = MutableStateFlow(NewsState())
    val newsState = _newsState.asStateFlow()

    init {
        getNews()
    }

    fun getNews() {
        newsUseCase.getNews().onEach { result ->

            when(result) {

                is Resource.Loading -> {
                    _newsState.value = NewsState(
                        isLoading = true
                    )
                }

                is Resource.Success -> {
                    _newsState.value = NewsState(
                        newsList = result.data ?: emptyList()
                    )
                }

                is Resource.Error -> {
                    _newsState.value = NewsState(
                        error = result.message ?: "Something went wrong"
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}