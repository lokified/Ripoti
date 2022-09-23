package com.loki.ripoti.domain.repository

import com.loki.ripoti.data.remote.response.Comments
import com.loki.ripoti.data.remote.response.Reports
import com.loki.ripoti.data.remote.response.UserResponse
import com.loki.ripoti.domain.model.Comment

interface CommentsRepository {

    suspend fun addComment(comment: Comment): UserResponse
    suspend fun getCommentsInReport(reportId: Int): List<Comments>
}