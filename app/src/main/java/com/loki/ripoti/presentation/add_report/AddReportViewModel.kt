package com.loki.ripoti.presentation.add_report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.ripoti.domain.model.Report
import com.loki.ripoti.domain.useCases.reports.ReportsUseCase
import com.loki.ripoti.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddReportViewModel @Inject constructor(
    private val reportsUseCase: ReportsUseCase
): ViewModel() {

    private val _state = MutableStateFlow(AddReportState())
    val state = _state.asStateFlow()

    fun addReport(userId: Int, report: Report) {

        reportsUseCase.addReport(userId, report).onEach { result ->

            when(result) {

                is Resource.Loading -> {
                    _state.value = AddReportState(
                        isLoading = true
                    )
                }

                is Resource.Success -> {
                    _state.value = AddReportState(
                        message = result.data?.message!!
                    )
                }

                is Resource.Error -> {
                    _state.value = AddReportState(
                        error = result.message ?: "Something went wrong!"
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}