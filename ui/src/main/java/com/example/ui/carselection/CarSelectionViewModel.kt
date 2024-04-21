package com.example.ui.carselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.di.DefaultDispatcher
import com.example.domain.model.Car
import com.example.domain.model.FuelType
import com.example.domain.model.SelectedCar
import com.example.domain.usecase.AddSelectedCarUseCase
import com.example.domain.usecase.GetCarsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarSelectionViewModel @Inject constructor(
    getCarsUseCase: GetCarsUseCase,
    private val addSelectedCarUseCase: AddSelectedCarUseCase,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
    private lateinit var carList: List<Car>
    private var selectedBrand = ""
    private var selectedSeries = ""
    private var selectedModelYear = ""
    private var selectedFuelType: FuelType? = null

    private val actions: MutableSharedFlow<CarSelectionAction> =
        MutableSharedFlow(extraBufferCapacity = 64)

    private val _events = Channel<CarSelectionUiEvent>(capacity = 32)
    val events: Flow<CarSelectionUiEvent> = _events.receiveAsFlow()

    private val _viewState: MutableStateFlow<CarSelectionUiState> =
        MutableStateFlow(CarSelectionUiState.Loading)

    val viewState: StateFlow<CarSelectionUiState>
        get() = _viewState

    init {
        actions
            .onEach(::handleAction)
            .flowOn(defaultDispatcher)
            .launchIn(viewModelScope)

        viewModelScope.launch {
            getCarsUseCase.get()
                .catch {
                    _viewState.update { CarSelectionUiState.Error }
                }.distinctUntilChanged()
                .mapLatest(::mapCarsListResultToUiState)
                .collect { uiState ->
                    _viewState.update { uiState }
                }
        }
    }

    fun onAction(action: CarSelectionAction) = actions.tryEmit(action)

    private suspend fun handleAction(action: CarSelectionAction) {
        when (action) {
            is CarSelectionAction.OnBrandSelected -> {
                selectedBrand = action.brand
                updateToSelectSeriesUiState()
            }

            is CarSelectionAction.OnSeriesSelected -> {
                selectedSeries = action.series
                updateToSelectYearUiState()
            }

            is CarSelectionAction.OnBuildYearSelected -> {
                selectedModelYear = action.modelYear
                updateToSelectFuelTypeUiState()
            }

            is CarSelectionAction.OnFuelTypeSelected -> {
                selectedFuelType = FuelType.valueOf(action.fuelType)
                afterFuelTypeSelectAction()
            }

            CarSelectionAction.OnSelectionFinished -> _events.send(CarSelectionUiEvent.NavigateToDashboard)

            CarSelectionAction.SearchIconClicked -> _events.send(CarSelectionUiEvent.NavigateToSearch)
            is CarSelectionAction.UpPressed -> handleUpPressAction(action.currentUiState)
            is CarSelectionAction.OnBackPressed -> handleUpPressAction(action.currentUiState)
        }
    }

    private suspend fun handleUpPressAction(currentUiState: CarSelectionUiState) {
        when (currentUiState) {
            is CarSelectionUiState.SeriesSelection -> updateToSelectBrandUiState()
            is CarSelectionUiState.BuildYearSelection -> updateToSelectSeriesUiState()
            is CarSelectionUiState.FuelTypeSelection -> updateToSelectYearUiState()

            is CarSelectionUiState.BrandSelection -> _events.send(CarSelectionUiEvent.GoBack)
            CarSelectionUiState.CarSelectionFinished -> _events.send(CarSelectionUiEvent.GoBack)
            CarSelectionUiState.Error -> _events.send(CarSelectionUiEvent.GoBack)
            CarSelectionUiState.Loading -> _events.send(CarSelectionUiEvent.GoBack)
        }
    }

    private fun mapCarsListResultToUiState(cars: List<Car>): CarSelectionUiState =
        if (cars.isNotEmpty()) {
            carList = cars
            CarSelectionUiState.BrandSelection(cars.getBrands())
        } else {
            CarSelectionUiState.Error
        }

    private fun updateToSelectBrandUiState() {
        _viewState.update {
            CarSelectionUiState.BrandSelection(carList.getBrands())
        }
    }

    private fun updateToSelectSeriesUiState() {
        _viewState.update {
            CarSelectionUiState.SeriesSelection(
                selectedBrand,
                carList.getSeriesForBrand(selectedBrand)
            )
        }
    }

    private fun updateToSelectYearUiState() {
        val car = carList.getCarForBrandAndSeries(selectedBrand, selectedSeries)
        _viewState.update {
            CarSelectionUiState.BuildYearSelection(
                selectedBrand,
                selectedSeries,
                car.minSupportedYear,
                car.maxSupportedYear
            )
        }
    }

    private fun updateToSelectFuelTypeUiState() {
        _viewState.update {
            CarSelectionUiState.FuelTypeSelection(
                selectedBrand,
                selectedSeries,
                selectedModelYear = selectedModelYear
            )
        }
    }

    private fun afterFuelTypeSelectAction() {
        val fuelType = selectedFuelType ?: return
        val car = carList.first { it.brand == selectedBrand && it.series == selectedSeries }
        val selectedCar = SelectedCar(
            brand = selectedBrand,
            series = selectedSeries,
            buildYear = selectedModelYear.toInt(),
            fuelType = fuelType,
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
