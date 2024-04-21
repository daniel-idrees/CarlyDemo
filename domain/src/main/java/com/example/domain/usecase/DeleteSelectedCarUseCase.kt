package com.example.domain.usecase

import com.example.domain.model.SelectedCar
import com.example.domain.repository.CarRepository
import javax.inject.Inject

class DeleteSelectedCarUseCase @Inject constructor(
    private val carRepository: CarRepository
) {
    suspend fun delete(selectedCar: SelectedCar) = carRepository.deleteSelectedCar(selectedCar)
}