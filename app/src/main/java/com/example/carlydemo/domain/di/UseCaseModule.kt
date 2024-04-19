package com.example.carlydemo.domain.di

import com.example.carlydemo.domain.repository.CarRepository
import com.example.carlydemo.domain.usecase.GetCarsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal class UseCaseModule {

    @Provides
    @ViewModelScoped
    fun providesGetCarsUseCase(
        carRepository: CarRepository,
    ): GetCarsUseCase = GetCarsUseCase(carRepository)
}
