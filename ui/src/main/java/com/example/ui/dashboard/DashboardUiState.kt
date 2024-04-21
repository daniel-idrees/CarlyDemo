package com.example.ui.dashboard

import com.example.domain.model.SelectedCar

sealed interface DashboardUiState {
    data object NoCarSelectedState : DashboardUiState
    data class CarSelectedState(val car: SelectedCar): DashboardUiState
    data object Loading: DashboardUiState
}
