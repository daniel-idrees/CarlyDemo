package com.example.ui.carselection


sealed interface CarSelectionAction {

    data object SearchIconClicked : CarSelectionAction
    data class OnBackPressed(val currentUiState: CarSelectionUiState) : CarSelectionAction
    data class UpPressed(val currentUiState: CarSelectionUiState) : CarSelectionAction
    data class OnBrandSelected(val brand: String) : CarSelectionAction
    data class OnSeriesSelected(val series: String) : CarSelectionAction
    data class OnBuildYearSelected(val modelYear: String) : CarSelectionAction
    data class OnFuelTypeSelected(val fuelType: String) : CarSelectionAction
    data object OnSelectionFinished: CarSelectionAction
}
