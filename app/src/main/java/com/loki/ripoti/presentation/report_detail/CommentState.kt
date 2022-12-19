package com.loki.ripoti.presentation.report_detail

import com.loki.ripoti.data.remote.response.Comments

data class CommentState(
    val error: String = "",
    val message: String = "",
    val comment: List<Comments> = emptyList(),
    val isLoading: Boolean = false
)
