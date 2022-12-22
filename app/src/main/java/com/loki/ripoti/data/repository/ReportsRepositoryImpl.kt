package com.loki.ripoti.data.repository

import com.loki.ripoti.data.remote.RipotiApi
import com.loki.ripoti.data.remote.response.Reports
import com.loki.ripoti.data.remote.response.UserResponse
import com.loki.ripoti.domain.model.Report
import com.loki.ripoti.domain.repository.ReportsRepository
import javax.inject.Inject

class ReportsRepositoryImpl @Inject constructor(
    private val api: RipotiApi
): ReportsRepository {

    override suspend fun addReport(userId: Int, report: Report): UserResponse {
        return api.addReport(userId, report)
    }

    override suspend fun getReports(): List<Reports> {
        return api.getReports()
    }

    override suspend fun updateReport(id: Int, description: String): UserResponse {
        return api.updateReport(id, description)
    }

    override suspend fun deleteReport(id: Int): UserResponse {
        return api.deleteReport(id)
    }
}