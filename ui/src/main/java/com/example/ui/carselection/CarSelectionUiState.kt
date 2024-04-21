package com.example.ui.carselection

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

    data object CarSelectionFinished : CarSelectionUiState

    data object Error: CarSelectionUiState

    data object Loading: CarSelectionUiState
}