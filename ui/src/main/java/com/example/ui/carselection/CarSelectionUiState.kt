package com.example.ui.carselection

sealed interface CarSelectionUiState {
    data class BrandSelection(val brandsToSelect: List<String>) : CarSelectionUiState
    data class SeriesSelection(
        val selectedBrand: String,
        val seriesToSelect: List<String>
    ) : CarSelectionUiState

    data class BuildYearSelection(
        val selectedBrand: String,
        val selectedSeries: String,
        val buildYearsToSelect: List<String>
    ) : CarSelectionUiState

    data class FuelTypeSelection(
        val selectedBrand: String,
        val selectedSeries: String,
        val selectedModelYear: String,
        val fuelTypes: List<String>
    ) : CarSelectionUiState

    data object Error: CarSelectionUiState

    data object Loading: CarSelectionUiState
}