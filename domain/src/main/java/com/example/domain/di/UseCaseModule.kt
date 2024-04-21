package com.example.domain.di

import com.example.core.di.DefaultDispatcher
import com.example.data.repository.CarRepository
import com.example.domain.usecase.AddSelectedCarUseCase
import com.example.domain.usecase.DeleteSelectedCarUseCase
import com.example.domain.usecase.GetCarsUseCase
import com.example.domain.usecase.GetMainSelectedCarUseCase
import com.example.domain.usecase.GetSelectedCarsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
internal class UseCaseModule {

    @Provides
    @ViewModelScoped
    fun providesGetCarsUseCase(
        carRepository: CarRepository,
    ): GetCarsUseCase = GetCarsUseCase(carRepository)

    @Provides
    @ViewModelScoped
    fun providesAddSelectedCarUseCase(
        carRepository: CarRepository,
    ): AddSelectedCarUseCase = AddSelectedCarUseCase(carRepository)

    @Provides
    @ViewModelScoped
    fun providesDeleteSelectedCarUseCase(
        carRepository: CarRepository,
    ): DeleteSelectedCarUseCase = DeleteSelectedCarUseCase(carRepository)

    @Provides
    @ViewModelScoped
    fun providesGetMainSelectedCarUseCase(
        carRepository: CarRepository,
    ): GetMainSelectedCarUseCase = GetMainSelectedCarUseCase(carRepository)

    @Provides
    @ViewModelScoped
    fun providesGetSelectedCarsUseCase(
        carRepository: CarRepository,
    ): GetSelectedCarsUseCase = GetSelectedCarsUseCase(carRepository)
}
