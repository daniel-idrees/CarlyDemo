package com.example.ui.carlist

import com.example.domain.model.SelectedCar

sealed interface CarListUiState {
    data class SelectedCars(val selectedCars: List<SelectedCar>) : CarListUiState
    data object Loading : CarListUiState
    data object Error : CarListUiState
}
