package com.loki.ripoti.domain.repository

import com.loki.ripoti.data.remote.response.Reports
import com.loki.ripoti.data.remote.response.UserResponse
import com.loki.ripoti.domain.model.Report

interface ReportsRepository {

    suspend fun addReport(userId: Int, report: Report): UserResponse

    suspend fun getReports(): List<Reports>

    suspend fun updateReport(id: Int, description: String): UserResponse

    suspend fun deleteReport(id: Int): UserResponse
}