package com.example.domain.usecase

import com.example.data.repository.SelectedCarRepository
import javax.inject.Inject

class SetCarAsMainUseCase @Inject constructor(
    private val repository: SelectedCarRepository
) {
    suspend fun set(id: Long?) = repository.setCarAsMain(id)
}
