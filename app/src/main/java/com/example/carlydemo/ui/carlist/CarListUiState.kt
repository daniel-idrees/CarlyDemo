package com.example.carlydemo.ui.carlist

import com.example.carlydemo.domain.model.SelectedCar

sealed interface CarListUiState {
    data class SelectedCars(val selectedCars: List<SelectedCar>) : CarListUiState
}
