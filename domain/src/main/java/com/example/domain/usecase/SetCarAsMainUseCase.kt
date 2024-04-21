package com.example.domain.usecase

import com.example.data.repository.CarRepository
import javax.inject.Inject

class SetCarAsMainUseCase @Inject constructor(
    private val carRepository: CarRepository
) {
    suspend fun set(id: Long?) = carRepository.setCarAsMain(id)
}