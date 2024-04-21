package com.example.ui.carlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.di.IoDispatcher
import com.example.domain.model.SelectedCar
import com.example.domain.usecase.provider.CarListUseCaseProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarListViewModel @Inject constructor(
    private val carListUseCaseProvider: CarListUseCaseProvider,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _viewState: MutableStateFlow<CarListUiState> =
        MutableStateFlow(CarListUiState.Loading)

    val viewState: StateFlow<CarListUiState>
        get() = _viewState

    init {
        viewModelScope.launch {
            carListUseCaseProvider.getSelectedCarsUseCase.get()
                .flowOn(ioDispatcher)
                .catch { _viewState.update { CarListUiState.Error } }
                .collect { cars ->
                    _viewState.update { CarListUiState.SelectedCars(cars) }
                }
        }
    }

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