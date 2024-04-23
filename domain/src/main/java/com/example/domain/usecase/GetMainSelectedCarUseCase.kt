package com.example.domain.usecase

import com.example.data.repository.SelectedCarRepository
import com.example.domain.model.mapper.toSelectedCar
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMainSelectedCarUseCase @Inject constructor(
    private val repository: SelectedCarRepository
) {
    fun get() = repository.getMainSelectedCar().map { entity ->
        entity?.toSelectedCar()
    }
}