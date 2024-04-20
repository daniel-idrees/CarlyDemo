package com.example.carlydemo.ui.carselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carlydemo.domain.model.Car
import com.example.carlydemo.domain.model.FuelType
import com.example.carlydemo.domain.model.SelectedCar
import com.example.carlydemo.domain.usecase.AddSelectedCarUseCase
import com.example.carlydemo.domain.usecase.GetCarsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarSelectionViewModel @Inject constructor(
    carsUseCase: GetCarsUseCase,
    private val addSelectedCarUseCase: AddSelectedCarUseCase
) : ViewModel() {
    private lateinit var carList: List<Car>
    private var selectedBrand = ""
    private var selectedSeries = ""
    private var selectedModelYear = ""
    private var selectedFuelType: FuelType? = null
    private val _viewState: MutableStateFlow<CarSelectionUiState> =
        MutableStateFlow(CarSelectionUiState.Loading)

    val viewState: StateFlow<CarSelectionUiState>
        get() = _viewState

    init {
        viewModelScope.launch {
            carsUseCase.get()
                .flowOn(Dispatchers.IO)
                .catch { _viewState.update { CarSelectionUiState.Error } }
                .map { cars ->
                    cars?.let { list ->
                        carList = list
                        CarSelectionUiState.SelectBrand(list.getBrands())
                    } ?: CarSelectionUiState.Error
                }.collect { uiState ->
                    _viewState.update { uiState }
                }
        }
    }

    fun initialState() {
        _viewState.update {
            CarSelectionUiState.SelectBrand(carList.getBrands())
        }
    }

    fun selectBrand(brand: String) {
        selectedBrand = brand
        _viewState.update {
            CarSelectionUiState.SelectSeries(
                brand,
                carList.getSeriesForBrand(selectedBrand)
            )
        }
    }

    fun selectSeries(series: String) {
        selectedSeries = series
        val car = carList.getCarForBrandAndSeries(selectedBrand, selectedSeries)
        _viewState.update {
            CarSelectionUiState.SelectYear(
                selectedBrand,
                selectedSeries,
                car.minSupportedYear,
                car.maxSupportedYear
            )
        }
    }

    fun selectModelYear(modelYear: String) {
        selectedModelYear = modelYear
        _viewState.update {
            CarSelectionUiState.SelectFuelType(
                selectedBrand,
                selectedSeries,
                selectedModelYear = modelYear
            )
        }
    }

    fun selectFuelType(fuelType: String) {
        selectedFuelType = FuelType.valueOf(fuelType)
        val car = carList.first { it.brand == selectedBrand && it.series == selectedSeries }
        val selectedCar = SelectedCar(
            brand = selectedBrand,
            series = selectedSeries,
            buildYear = selectedModelYear.toInt(),
            fuelType = FuelType.valueOf(fuelType),
            features = car.features,
            isMain = true
        )

        viewModelScope.launch {
            addSelectedCarUseCase.add(selectedCar)
            _viewState.update { CarSelectionUiState.CarSelectionFinished }
        }
    }
}

private fun List<Car>.getBrands(): List<String> = map { it.brand }.distinct()

private fun List<Car>.getSeriesForBrand(brand: String): List<String> =
    filter { it.brand == brand }.map { it.series }

private fun List<Car>.getCarForBrandAndSeries(brand: String, series: String): Car =
    first { it.brand == brand && it.series == series }
