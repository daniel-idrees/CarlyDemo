package com.example.domain.usecase

import com.example.data.repository.SelectedCarRepository
import com.example.domain.model.SelectedCar
import com.example.domain.model.mapper.asEntity
import javax.inject.Inject

class AddSelectedCarUseCase @Inject constructor(
    private val repository: SelectedCarRepository
) {
    suspend fun add(selectedCar: SelectedCar) = repository.addSelectedCar(selectedCar.asEntity())
}
