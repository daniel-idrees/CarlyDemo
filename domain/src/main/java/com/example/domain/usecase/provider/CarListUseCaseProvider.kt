package com.example.domain.usecase.provider

import com.example.domain.usecase.DeleteSelectedCarUseCase
import com.example.domain.usecase.GetSelectedCarsUseCase
import com.example.domain.usecase.SetCarAsMainUseCase
import javax.inject.Inject

class CarListUseCaseProvider @Inject constructor(
    val getSelectedCarsUseCase: GetSelectedCarsUseCase,
    val deleteSelectedCarUseCase: DeleteSelectedCarUseCase,
    val setCarAsMainUseCase: SetCarAsMainUseCase,
)
