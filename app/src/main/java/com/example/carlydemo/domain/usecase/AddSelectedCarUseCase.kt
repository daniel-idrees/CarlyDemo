package com.example.carlydemo.domain.usecase

import com.example.carlydemo.domain.model.SelectedCar
import com.example.carlydemo.domain.repository.CarRepository
import javax.inject.Inject

class AddSelectedCarUseCase @Inject constructor(
    private val carRepository: CarRepository
) {
    suspend fun add(selectedCar: SelectedCar) = carRepository.addSelectedCar(selectedCar)
}
