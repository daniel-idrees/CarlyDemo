package com.example.ui.carselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.di.DefaultDispatcher
import com.example.domain.model.Car
import com.example.domain.model.FuelType
import com.example.domain.model.SelectedCar
import com.example.domain.model.util.getBrands
import com.example.domain.model.util.getCarForBrandAndSeries
import com.example.domain.model.util.getSeriesForBrand
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

    private var currentBrandList = listOf<String>()
    private var currentSeriesList = listOf<String>()
    private var currentYearList = listOf<String>()
    private lateinit var currentCar: Car

    private val headerTextItems = mutableListOf<String>()

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

    private fun mapCarsListResultToUiState(cars: List<Car>): CarSelectionUiState =
        if (cars.isNotEmpty()) {
            carList = cars
            currentBrandList = cars.getBrands()
            CarSelectionUiState.BrandSelection(currentBrandList)
        } else {
            CarSelectionUiState.Error
        }

    fun onAction(action: CarSelectionAction) = actions.tryEmit(action)

    private suspend fun handleAction(action: CarSelectionAction) {
        when (action) {
            CarSelectionAction.SearchTextEmpty -> updateViewStateWithOriginalList()

            is CarSelectionAction.OnBrandSelected -> {
                selectedBrand = action.brand
                headerTextItems.add(selectedBrand)
                updateToSelectSeriesUiState()
            }

            is CarSelectionAction.OnSeriesSelected -> {
                selectedSeries = action.series
                headerTextItems.add(selectedSeries)
                updateToSelectYearUiState()
            }

            is CarSelectionAction.OnBuildYearSelected -> {
                selectedModelYear = action.modelYear
                headerTextItems.add(selectedModelYear)
                updateToSelectFuelTypeUiState()
            }

            is CarSelectionAction.OnFuelTypeSelected -> {
                selectedFuelType = FuelType.valueOf(action.fuelType)
                afterFuelTypeSelectAction()
            }

            CarSelectionAction.OnSelectionFinished -> _events.send(CarSelectionUiEvent.NavigateToDashboard)

            is CarSelectionAction.SearchAction -> {
                handleSearchAction(action.searchText)
            }

            is CarSelectionAction.UpPressed -> handleUpPressAction()
            is CarSelectionAction.OnBackPressed -> handleUpPressAction()
        }
    }

    private fun updateViewStateWithOriginalList() {
        when (viewState.value) {
            is CarSelectionUiState.BrandSelection -> _viewState.update {
                CarSelectionUiState.BrandSelection(currentBrandList)
            }

            is CarSelectionUiState.BuildYearSelection ->
                _viewState.update {
                    CarSelectionUiState.BuildYearSelection(
                        selectedBrand,
                        selectedSeries,
                        currentYearList
                    )
                }

            is CarSelectionUiState.FuelTypeSelection -> _viewState.update {
                CarSelectionUiState.FuelTypeSelection(
                    selectedBrand,
                    selectedSeries,
                    selectedModelYear = selectedModelYear,
                    FuelType.getList()
                )
            }

            is CarSelectionUiState.SeriesSelection -> _viewState.update {
                CarSelectionUiState.SeriesSelection(
                    selectedBrand,
                    currentSeriesList
                )
            }

            else -> {
                //no-op
            }
        }
    }

    private fun handleSearchAction(searchText: String) {
        when (viewState.value) {
            is CarSelectionUiState.SeriesSelection -> {
                (viewState.value as? CarSelectionUiState.SeriesSelection)?.let {
                    searchOnSeriesList(searchText)
                }
            }

            is CarSelectionUiState.BuildYearSelection ->
                (viewState.value as? CarSelectionUiState.BuildYearSelection)?.let {
                    searchActionOnBuildYearList(
                        searchText
                    )
                }

            is CarSelectionUiState.FuelTypeSelection -> (viewState.value as? CarSelectionUiState.FuelTypeSelection)?.let {
                searchActionOnFuelTypeList(searchText)
            }

            is CarSelectionUiState.BrandSelection -> (viewState.value as? CarSelectionUiState.BrandSelection)?.let {
                searchActionOnBrandList(searchText)
            }

            else -> {
                //no-op
            }
        }
    }

    private fun searchOnSeriesList(searchText: String) {
        val matchedList = currentSeriesList.getMatchedList(searchText)
        _viewState.update {
            CarSelectionUiState.SeriesSelection(
                selectedBrand,
                matchedList
            )
        }
    }

    private fun searchActionOnBuildYearList(searchText: String) {
        val searchNumber = searchText.toIntOrNull()

        if (searchNumber == null) {
            _viewState.update {
                CarSelectionUiState.BuildYearSelection(
                    selectedBrand,
                    selectedSeries,
                    emptyList(),
                )
            }
            return
        }

        val matchedList = currentYearList.getMatchedList(searchText).map { it }
        _viewState.update {
            CarSelectionUiState.BuildYearSelection(
                selectedBrand,
                selectedSeries,
                matchedList
            )
        }
    }

    private fun searchActionOnFuelTypeList(searchText: String) {
        val matchedList = FuelType.getList().getMatchedList(searchText)
        _viewState.update {
            CarSelectionUiState.FuelTypeSelection(
                selectedBrand,
                selectedSeries,
                selectedModelYear,
                matchedList
            )
        }
    }

    private fun searchActionOnBrandList(searchText: String) {
        val matchedList = currentBrandList.getMatchedList(searchText)
        _viewState.update { CarSelectionUiState.BrandSelection(matchedList) }
    }

    private suspend fun handleUpPressAction() {
        headerTextItems.removeLastOrNull()

        when (viewState.value) {
            is CarSelectionUiState.SeriesSelection -> updateToSelectBrandUiState()
            is CarSelectionUiState.BuildYearSelection -> updateToSelectSeriesUiState()
            is CarSelectionUiState.FuelTypeSelection -> updateToSelectYearUiState()

            is CarSelectionUiState.BrandSelection -> _events.send(CarSelectionUiEvent.NavigateBack)
            CarSelectionUiState.Error -> _events.send(CarSelectionUiEvent.NavigateBack)
            CarSelectionUiState.Loading -> _events.send(CarSelectionUiEvent.NavigateBack)
        }
    }

    private suspend fun updateToSelectBrandUiState() {
        sendUpdateHeaderTextEvent()
        _viewState.update {
            CarSelectionUiState.BrandSelection(currentBrandList)
        }
    }

    private suspend fun updateToSelectSeriesUiState() {
        sendUpdateHeaderTextEvent()
        currentSeriesList = carList.getSeriesForBrand(selectedBrand)
        _viewState.update {
            CarSelectionUiState.SeriesSelection(
                selectedBrand,
                currentSeriesList
            )
        }
    }

    private suspend fun updateToSelectYearUiState() {
        currentCar = carList.getCarForBrandAndSeries(selectedBrand, selectedSeries)
        currentYearList =
            (currentCar.minSupportedYear..currentCar.maxSupportedYear).map { it.toString() }
        sendUpdateHeaderTextEvent()

        _viewState.update {
            CarSelectionUiState.BuildYearSelection(
                selectedBrand,
                selectedSeries,
                currentYearList,
            )
        }
    }

    private suspend fun updateToSelectFuelTypeUiState() {
        sendUpdateHeaderTextEvent()
        _viewState.update {
            CarSelectionUiState.FuelTypeSelection(
                selectedBrand,
                selectedSeries,
                selectedModelYear = selectedModelYear,
                FuelType.getList()
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
            _events.send(CarSelectionUiEvent.NavigateToDashboard)
        }
    }

    private suspend fun sendUpdateHeaderTextEvent() {
        _events.send(CarSelectionUiEvent.UpdateHeaderText(headerTextItems.joinToString(", ")))
    }
}

private fun List<String>.getMatchedList(searchText: String) =
    filter { it.startsWith(searchText, true) }
