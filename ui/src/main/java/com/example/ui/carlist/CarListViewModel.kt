package com.example.ui.carlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.di.DefaultDispatcher
import com.example.domain.model.SelectedCar
import com.example.domain.usecase.provider.CarListUseCaseProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class CarListViewModel @Inject constructor(
    private val carListUseCaseProvider: CarListUseCaseProvider,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val actions: MutableSharedFlow<CarListAction> =
        MutableSharedFlow(extraBufferCapacity = 64)

    private val _events = Channel<CarListUiEvent>(capacity = 32)
    val events: Flow<CarListUiEvent> = _events.receiveAsFlow()

    val viewState: StateFlow<CarListUiState> =
        carListUseCaseProvider.getSelectedCarsUseCase.get()
            .catch { CarListUiState.Error }
            .distinctUntilChanged()
            .mapLatest { cars ->
                CarListUiState.SelectedCars(cars)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
                initialValue = CarListUiState.Loading
            )

    init {
        actions
            .onEach(::handleAction)
            .flowOn(defaultDispatcher)
            .launchIn(viewModelScope)
    }

    fun onAction(action: CarListAction) = actions.tryEmit(action)

    private suspend fun handleAction(action: CarListAction) {
        when (action) {
            CarListAction.AddButtonClicked -> _events.send(CarListUiEvent.NavigateToCarSelection)
            is CarListAction.CarItemClicked -> setCarAsMain(action.car)
            is CarListAction.DeleteIconClicked -> deleteCar(action.car)
            CarListAction.UpPressed -> _events.send(CarListUiEvent.goBack)
        }
    }

    private fun deleteCar(car: SelectedCar) {
        viewModelScope.launch {
            carListUseCaseProvider.deleteSelectedCarUseCase.delete(car)
        }
    }

    private fun setCarAsMain(car: SelectedCar) {
        viewModelScope.launch {
            carListUseCaseProvider.setCarAsMainUseCase.set(car.id)
        }
    }
}