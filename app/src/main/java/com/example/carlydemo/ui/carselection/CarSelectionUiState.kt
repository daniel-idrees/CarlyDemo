package com.example.carlydemo.ui.carselection

import com.example.carlydemo.domain.model.SelectedCar
import com.example.carlydemo.ui.dashboard.DashboardUiState

sealed interface CarSelectionUiState {
    data class SelectBrand(val brands: List<String>) : CarSelectionUiState
    data class SelectSeries(
        val selectedBrand: String,
        val series: List<String>
    ) : CarSelectionUiState

    data class SelectYear(
        val selectedBrand: String,
        val selectedSeries: String,
        val minSupportedYear: Int,
        val maxSupportedYear: Int
    ) : CarSelectionUiState

    data class SelectFuelType(
        val selectedBrand: String,
        val selectedSeries: String,
        val selectedModelYear: String
    ) : CarSelectionUiState

    data class CarSelectionFinished(
        val selectedCar: SelectedCar
    ): CarSelectionUiState

    data object Error: CarSelectionUiState

    data object Loading: CarSelectionUiState
}