package com.loki.ripoti.domain.useCases.comments

data class CommentsUseCase (
    val addComment: AddCommentUseCase,
    val getComments: GetCommentsUseCase
    )