package com.example.carlydemo.domain.usecase

import com.example.carlydemo.domain.repository.CarRepository
import javax.inject.Inject

class GetCarsUseCase @Inject constructor(
    private val carRepository: CarRepository
) {
    fun get() = carRepository.getCars()
}
