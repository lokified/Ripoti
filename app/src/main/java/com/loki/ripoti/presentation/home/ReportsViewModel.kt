package com.loki.ripoti.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.ripoti.domain.model.Report
import com.loki.ripoti.domain.useCases.reports.ReportsUseCase
import com.loki.ripoti.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class  ReportsViewModel @Inject constructor(
    private val reportsUseCase: ReportsUseCase
): ViewModel() {

    private val _state = MutableLiveData(ReportState())
    val state: LiveData<ReportState> = _state

    init {
        getReports()
    }

    fun getReports() {

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