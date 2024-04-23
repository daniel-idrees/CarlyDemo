package com.example.domain.usecase

import com.example.data.repository.SelectedCarRepository
import com.example.domain.model.SelectedCar
import com.example.domain.model.mapper.asEntity
import javax.inject.Inject

class DeleteSelectedCarUseCase @Inject constructor(
    private val repository: SelectedCarRepository
) {
    suspend fun delete(selectedCar: SelectedCar) = repository.deleteSelectedCar(selectedCar.asEntity())
}