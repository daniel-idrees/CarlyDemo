package com.example.domain.usecase

import com.example.data.database.entity.CarEntity
import com.example.data.repository.SelectedCarRepository
import com.example.domain.model.mapper.toSelectedCar
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSelectedCarsUseCase @Inject constructor(
    private val repository: SelectedCarRepository
) {
    fun get() = repository.getSelectedCars().map { entities ->
        entities.map(CarEntity::toSelectedCar)
    }
}
