package com.loki.ripoti.domain.useCases.reports

data class ReportsUseCase(
    val addReport: AddReportUseCase,
    val getReports: GetReportsUseCase,
    val updateReport: UpdateReportUseCase,
    val deleteReport: DeleteReportUseCase
)
