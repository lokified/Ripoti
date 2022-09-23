package com.loki.ripoti.data.remote

import com.loki.ripoti.data.remote.response.Comments
import com.loki.ripoti.data.remote.response.LoginResponse
import com.loki.ripoti.data.remote.response.Reports
import com.loki.ripoti.data.remote.response.UserResponse
import com.loki.ripoti.domain.model.Comment
import com.loki.ripoti.domain.model.Login
import com.loki.ripoti.domain.model.Report
import com.loki.ripoti.domain.model.User
import retrofit2.http.*

interface RipotiApi {

    @POST("registerUser")
    suspend fun registerUser(
        @Body user: User
    ): UserResponse

    @POST("userLogin")
    suspend fun loginUser(
        @Body login: Login
    ): LoginResponse

    @GET("getReports")
    suspend fun getReports(): List<Reports>

    @POST("postReport/{userId}")
    suspend fun addReport(
        @Path("userId") userId: Int,
        @Body report: Report
    ): UserResponse

    @PUT("updateReport/{id}")
    suspend fun updateReport(
        @Path("id") id: Int,
        @Body description: String
    ): UserResponse

    @DELETE("deleteReport/{id}")
    suspend fun deleteReport(
        @Path("id") id: Int
    ): UserResponse

    @POST("addComment/{userId}")
    suspend fun addComment(
        @Body comment: Comment
    ): UserResponse

    @GET("getCommentsInReport/{reportId}")
    suspend fun getCommentsInReport(
        @Path("reportId") reportId: Int
    ): List<Comments>
}