package com.example.domain.usecase

import com.example.data.database.entity.CarEntity
import com.example.data.repository.CarRepository
import com.example.domain.model.mapper.toSelectedCar
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSelectedCarsUseCase @Inject constructor(
    private val carRepository: CarRepository
) {
    fun get() = carRepository.getSelectedCars().map { entities -> entities.map(CarEntity::toSelectedCar) }
}
