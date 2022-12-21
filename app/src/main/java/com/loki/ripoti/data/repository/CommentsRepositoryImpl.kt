package com.loki.ripoti.data.repository

import com.loki.ripoti.data.remote.RipotiApi
import com.loki.ripoti.data.remote.response.Comments
import com.loki.ripoti.data.remote.response.Reports
import com.loki.ripoti.data.remote.response.UserResponse
import com.loki.ripoti.domain.model.Comment
import com.loki.ripoti.domain.repository.CommentsRepository
import javax.inject.Inject

class CommentsRepositoryImpl @Inject constructor(
    private val api: RipotiApi
): CommentsRepository {

    override suspend fun addComment(userId: Int, comment: Comment): UserResponse {
        return api.addComment(userId, comment)
    }

    override suspend fun getCommentsInReport(reportId: Int): List<Comments> {
        return api.getCommentsInReport(reportId)
    }
}