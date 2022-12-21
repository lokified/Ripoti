package com.loki.ripoti.presentation.home

import com.loki.ripoti.data.remote.response.UserReports

data class ReportState(
    val error: String = "",
    val message: String = "",
    val reports: List<UserReports> = emptyList(),
    val isLoading: Boolean = false
)
