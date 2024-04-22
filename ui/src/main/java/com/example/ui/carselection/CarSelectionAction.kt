package com.example.ui.carselection


sealed interface CarSelectionAction {

    data class SearchIconClicked(val searchText: String) : CarSelectionAction
    data object SearchTextEmpty : CarSelectionAction
    data object OnBackPressed : CarSelectionAction
    data object UpPressed : CarSelectionAction
    data class OnBrandSelected(val brand: String) : CarSelectionAction
    data class OnSeriesSelected(val series: String) : CarSelectionAction
    data class OnBuildYearSelected(val modelYear: String) : CarSelectionAction
    data class OnFuelTypeSelected(val fuelType: String) : CarSelectionAction
    data object OnSelectionFinished: CarSelectionAction
}
