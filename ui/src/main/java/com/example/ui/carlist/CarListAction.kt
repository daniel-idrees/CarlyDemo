package com.example.ui.carlist

import com.example.domain.model.SelectedCar


sealed interface CarListAction {
    data class DeleteIconClicked(val car: SelectedCar) : CarListAction
    data class CarItemClicked(val car: SelectedCar) : CarListAction
    data object AddButtonClicked : CarListAction
    data object UpPressed: CarListAction
}
