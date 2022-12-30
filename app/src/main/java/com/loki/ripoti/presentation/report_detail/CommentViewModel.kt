package com.loki.ripoti.presentation.report_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.ripoti.domain.model.Comment
import com.loki.ripoti.domain.useCases.comments.CommentsUseCase
import com.loki.ripoti.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val commentsUseCase: CommentsUseCase
): ViewModel() {

    private val _state = MutableStateFlow(CommentState())
    val state = _state.asStateFlow()

    private val _addCommentEvent = MutableSharedFlow<AddCommentEvent>()
    val addCommentEvent = _addCommentEvent.asSharedFlow()

    fun addComment(userId: Int, comment: Comment) {

        commentsUseCase.addComment(userId, comment).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _addCommentEvent.emit(AddCommentEvent.IsLoading)
                }
                is Resource.Success -> {
                    if (result.data?.message == "comment added"){
                        _addCommentEvent.emit(AddCommentEvent.Success)
                    }
                }
                is Resource.Error -> {
                    _addCommentEvent.emit(AddCommentEvent.ErrorAddComment(result.message ?: "something went wrong"))
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

    sealed class AddCommentEvent {
        object IsLoading: AddCommentEvent()
        data class ErrorAddComment(val error: String): AddCommentEvent()
        object Success: AddCommentEvent()
    }
}