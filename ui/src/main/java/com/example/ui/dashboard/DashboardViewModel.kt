package com.example.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetMainSelectedCarUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getMainSelectedCarUseCase: GetMainSelectedCarUseCase,
) : ViewModel() {

    val viewState: StateFlow<DashboardUiState> =
        getMainSelectedCarUseCase.get()
            .catch { DashboardUiState.NoCarSelectedState }
            .distinctUntilChanged()
            .filterNotNull()
            .mapLatest { car ->
                DashboardUiState.CarSelectedState(car)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
                initialValue = DashboardUiState.NoCarSelectedState
            )
}
