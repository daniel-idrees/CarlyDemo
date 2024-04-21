package com.example.domain.usecase

import com.example.domain.model.SelectedCar
import com.example.data.repository.CarRepository
import com.example.domain.model.mapper.asEntity
import javax.inject.Inject

class AddSelectedCarUseCase @Inject constructor(
    private val carRepository: CarRepository
) {
    suspend fun add(selectedCar: SelectedCar) = carRepository.addSelectedCar(selectedCar.asEntity())
}
