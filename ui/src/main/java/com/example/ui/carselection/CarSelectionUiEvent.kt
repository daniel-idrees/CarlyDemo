package com.example.ui.carselection


sealed interface CarSelectionUiEvent {
    data object NavigateToDashboard : CarSelectionUiEvent
    data object NavigateBack : CarSelectionUiEvent
    data class UpdateHeaderText(val text: String): CarSelectionUiEvent
}
