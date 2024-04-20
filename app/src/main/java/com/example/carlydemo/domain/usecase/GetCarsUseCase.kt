package com.example.carlydemo.domain.usecase

import com.example.carlydemo.domain.repository.CarRepository
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetCarsUseCase @Inject constructor(
    private val carRepository: CarRepository
) {
    suspend fun get() = flowOf(carRepository.getCars())
}
