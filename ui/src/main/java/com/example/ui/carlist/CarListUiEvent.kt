package com.example.ui.carlist


sealed interface CarListUiEvent {
    data object NavigateToCarSelection : CarListUiEvent
    data object goBack : CarListUiEvent
}
