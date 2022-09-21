package com.loki.ripoti.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.ripoti.domain.model.Report
import com.loki.ripoti.domain.useCases.reports.ReportsUseCase
import com.loki.ripoti.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val reportsUseCase: ReportsUseCase
): ViewModel() {

    private val _state = MutableLiveData(ReportState())
    val state: LiveData<ReportState> = _state

    init {
        getReports()
    }

    fun addReport(userId: Int, report: Report) {

        reportsUseCase.addReport(userId, report).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _state.value = ReportState(
                        isLoading = true
                    )
                }
                is Resource.Success -> {
                    _state.value = ReportState(
                        message = result.data?.message!!
                    )
                }
                is Resource.Error -> {
                    _state.value = ReportState(
                        error = result.message!!
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getReports() {

        reportsUseCase.getReports().onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _state.value = ReportState(
                        isLoading = true
                    )
                }
                is Resource.Success -> {
                    _state.value = ReportState(
                        reports = result.data!!
                    )
                }
                is Resource.Error -> {
                    _state.value = ReportState(
                        error = result.message!!
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}