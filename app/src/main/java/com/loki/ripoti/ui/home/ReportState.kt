package com.loki.ripoti.ui.home

import com.loki.ripoti.data.remote.response.Reports

data class ReportState(
    val error: String = "",
    val message: String = "",
    val reports: List<Reports> = emptyList(),
    val isLoading: Boolean = false
)
