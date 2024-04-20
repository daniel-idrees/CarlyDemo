package com.example.carlydemo.domain.usecase

import com.example.carlydemo.domain.model.SelectedCar
import com.example.carlydemo.domain.repository.CarRepository
import javax.inject.Inject

class DeleteSelectedCarUseCase @Inject constructor(
    private val carRepository: CarRepository
) {
    suspend fun delete(selectedCar: SelectedCar) = carRepository.deleteSelectedCar(selectedCar)
}