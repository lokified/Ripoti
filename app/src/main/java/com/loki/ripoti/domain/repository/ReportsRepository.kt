package com.loki.ripoti.domain.repository

import com.loki.ripoti.data.remote.response.UserReports
import com.loki.ripoti.data.remote.response.UserResponse
import com.loki.ripoti.domain.model.Report

interface ReportsRepository {

    suspend fun addReport(userId: Int, report: Report): UserResponse

    suspend fun getReports(): List<UserReports>

    suspend fun updateReport(id: Int, description: String): UserResponse

    suspend fun deleteReport(id: Int): UserResponse
}