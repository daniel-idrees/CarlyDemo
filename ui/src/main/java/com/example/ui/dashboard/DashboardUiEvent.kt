package com.example.ui.dashboard


sealed interface DashboardUiEvent {
    data object NavigateToFeatureScreen : DashboardUiEvent
    data object NavigateToCarList : DashboardUiEvent
    data object NavigateToCarSelection : DashboardUiEvent
}
