package com.loki.ripoti.ui.report_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.ripoti.domain.model.Comment
import com.loki.ripoti.domain.useCases.comments.CommentsUseCase
import com.loki.ripoti.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val commentsUseCase: CommentsUseCase
): ViewModel() {

    private val _state = MutableLiveData(CommentState())
    val state: LiveData<CommentState> = _state


    fun addComment(comment: Comment) {

        commentsUseCase.addComment(comment).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _state.value = CommentState(
                        isLoading = true
                    )
                }
                is Resource.Success -> {
                    _state.value = CommentState(
                        message = result.data?.message!!
                    )
                }
                is Resource.Error -> {
                    _state.value = CommentState(
                        error = result.message!!
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

     fun getComments(reportId: Int) {

        commentsUseCase.getComments(reportId).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _state.value = CommentState(
                        isLoading = true
                    )
                }
                is Resource.Success -> {
                    _state.value = CommentState(
                        comment = result.data!!
                    )
                }
                is Resource.Error -> {
                    _state.value = CommentState(
                        error = result.message!!
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}