package com.example.carlydemo.ui.carlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carlydemo.domain.model.SelectedCar
import com.example.carlydemo.domain.usecase.DeleteSelectedCarUseCase
import com.example.carlydemo.domain.usecase.GetSelectedCarsUseCase
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
class CarListViewModel @Inject constructor(
    private val getSelectedCarsUseCase: GetSelectedCarsUseCase,
    private val deleteSelectedCarUseCase: DeleteSelectedCarUseCase
): ViewModel() {
    private val _viewState: MutableStateFlow<CarListUiState> =
        MutableStateFlow(CarListUiState.Loading)

    val viewState: StateFlow<CarListUiState>
        get() = _viewState

    init {
        viewModelScope.launch {
            getSelectedCarsUseCase.get()
                .flowOn(Dispatchers.IO)
                .catch { CarListUiState.Error }
                .map { cars ->
                    cars.let { list ->
                        CarListUiState.SelectedCars(list)
                    }
                }.collect { uiState ->
                    _viewState.update { uiState }
                }
        }
    }

    fun deleteCar(car: SelectedCar) {
        viewModelScope.launch {
            deleteSelectedCarUseCase.delete(car)
        }
    }
}