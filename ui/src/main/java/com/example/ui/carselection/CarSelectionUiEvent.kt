package com.example.ui.carselection


sealed interface CarSelectionUiEvent {
    data object NavigateToSearch : CarSelectionUiEvent
    data object NavigateToDashboard : CarSelectionUiEvent
    data object GoBack : CarSelectionUiEvent
}
