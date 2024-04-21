package com.example.ui.carlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.SelectedCar
import com.example.domain.usecase.provider.CarListUseCaseProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class CarListViewModel @Inject constructor(
    private val carListUseCaseProvider: CarListUseCaseProvider,
) : ViewModel() {

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

    fun deleteCar(car: SelectedCar) {
        viewModelScope.launch {
            carListUseCaseProvider.deleteSelectedCarUseCase.delete(car)
        }
    }

    fun setCarAsMain(id: Long?) {
        viewModelScope.launch {
            carListUseCaseProvider.setCarAsMainUseCase.set(id)
        }
    }
}