package com.loki.ripoti.presentation.news.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.loki.ripoti.databinding.FragmentNewsBinding
import com.loki.ripoti.presentation.news.NewsViewModel
import com.loki.ripoti.util.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsFragment: Fragment() {

    private lateinit var binding: FragmentNewsBinding
    private lateinit var newsAdapter: NewsAdapter
    private val newsViewModel : NewsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.apply {

            newsAdapter = NewsAdapter()
            newsRecycler.adapter = newsAdapter
        }

        subscribeState()
    }

    private fun subscribeState() {

        lifecycleScope.launchWhenStarted {
            newsViewModel.newsState.collect { state ->
                binding.apply {
                    if (state.isLoading) {
                        hideOnLoading()
                        progressBar.isVisible = state.isLoading
                    }

                    if (state.newsList.isNotEmpty()) {
                        progressBar.isVisible = false
                        hideOnLoading()
                        newsAdapter.setNewsList(state.newsList)
                    }
                    else {
                        progressBar.isVisible = true
                        newsViewModel.getNews()
                    }

                    if (state.error.isNotEmpty()) {
                        progressBar.isVisible = false
                        showToast(state.error)

                        if (state.error == "check your internet connection") {
                            retryGettingNews()
                        }
                    }
                }
            }
        }
    }

    private fun retryGettingNews() {
        binding.apply {
            retryBtn.isVisible = true
            retryBtn.setOnClickListener {
                newsViewModel.getNews()
                progressBar.isVisible = true
            }
        }
    }

    private fun hideOnLoading() {
        binding.apply {
            retryBtn.visibility = View.GONE
            noNewsTxt.visibility = View.GONE
        }
    }
}