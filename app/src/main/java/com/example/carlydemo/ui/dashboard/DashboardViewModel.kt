package com.example.carlydemo.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carlydemo.domain.usecase.GetMainSelectedCarUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getMainSelectedCarUseCase: GetMainSelectedCarUseCase
) : ViewModel() {
    private val _viewState: MutableStateFlow<DashboardUiState> =
        MutableStateFlow(DashboardUiState.Loading)

    val viewState: StateFlow<DashboardUiState>
        get() = _viewState

    private var shouldRefreshData = true

    fun loadDataIfRequired() {
        if (shouldRefreshData) {
            _viewState.update { DashboardUiState.Loading }
            viewModelScope.launch {
                getMainSelectedCarUseCase.get()
                    .flowOn(Dispatchers.IO)
                    .catch {
                        _viewState.update { DashboardUiState.NoCarSelectedState }
                    }.collect { car ->
                        shouldRefreshData = false
                        _viewState.update { DashboardUiState.CarSelectedState(car) }
                    }
            }
        }
    }

    fun triggerTheDataLoadInNextResumption() {
        shouldRefreshData = true
    }
}
