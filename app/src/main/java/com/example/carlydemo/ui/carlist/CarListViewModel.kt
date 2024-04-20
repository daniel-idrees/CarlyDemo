package com.example.carlydemo.ui.carlist

import androidx.lifecycle.ViewModel
import com.example.carlydemo.domain.model.SelectedCar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CarListViewModel @Inject constructor(): ViewModel() {
    private val _viewState: MutableStateFlow<CarListUiState> =
        MutableStateFlow(CarListUiState.Loading)

    val viewState: StateFlow<CarListUiState>
        get() = _viewState

    init {
        //TODO load selected cars
    }

    fun deleteCar(car: SelectedCar) {
        //TODO delete from db
    }
}