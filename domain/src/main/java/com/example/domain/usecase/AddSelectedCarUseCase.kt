package com.example.domain.usecase

import com.example.domain.model.SelectedCar
import com.example.domain.repository.CarRepository
import javax.inject.Inject

class AddSelectedCarUseCase @Inject constructor(
    private val carRepository: CarRepository
) {
    suspend fun add(selectedCar: SelectedCar) = carRepository.addSelectedCar(selectedCar)
}
