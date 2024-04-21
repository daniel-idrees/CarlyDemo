package com.example.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.di.IoDispatcher
import com.example.domain.usecase.GetMainSelectedCarUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getMainSelectedCarUseCase: GetMainSelectedCarUseCase,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _viewState: MutableStateFlow<DashboardUiState> =
        MutableStateFlow(DashboardUiState.Loading)

    val viewState: StateFlow<DashboardUiState>
        get() = _viewState

    init {
        _viewState.update { DashboardUiState.Loading }
        viewModelScope.launch {
            getMainSelectedCarUseCase.get()
                .flowOn(ioDispatcher)
                .catch {
                    _viewState.update { DashboardUiState.NoCarSelectedState }
                }.collect {
                    it?.let { car ->
                        _viewState.update { DashboardUiState.CarSelectedState(car) }
                    } ?: _viewState.update { DashboardUiState.NoCarSelectedState }
                }
        }
    }
}
