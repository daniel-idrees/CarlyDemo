package com.example.domain.di

import com.example.data.repository.SelectedCarRepository
import com.example.data.repository.CarRepository
import com.example.domain.usecase.AddSelectedCarUseCase
import com.example.domain.usecase.DeleteSelectedCarUseCase
import com.example.domain.usecase.GetCarsUseCase
import com.example.domain.usecase.GetMainSelectedCarUseCase
import com.example.domain.usecase.GetSelectedCarsUseCase
import com.example.domain.usecase.SetCarAsMainUseCase
import com.example.domain.usecase.provider.CarListUseCaseProvider
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

    @Provides
    @ViewModelScoped
    fun providesAddSelectedCarUseCase(
        repository: SelectedCarRepository,
    ): AddSelectedCarUseCase = AddSelectedCarUseCase(repository)

    @Provides
    @ViewModelScoped
    fun providesDeleteSelectedCarUseCase(
        repository: SelectedCarRepository,
    ): DeleteSelectedCarUseCase = DeleteSelectedCarUseCase(repository)

    @Provides
    @ViewModelScoped
    fun providesGetMainSelectedCarUseCase(
        repository: SelectedCarRepository,
    ): GetMainSelectedCarUseCase = GetMainSelectedCarUseCase(repository)

    @Provides
    @ViewModelScoped
    fun providesGetSelectedCarsUseCase(
        repository: SelectedCarRepository,
    ): GetSelectedCarsUseCase = GetSelectedCarsUseCase(repository)

    @Provides
    @ViewModelScoped
    fun providesSetCarAsMainUseCase(
        repository: SelectedCarRepository,
    ): SetCarAsMainUseCase = SetCarAsMainUseCase(repository)

    @Provides
    @ViewModelScoped
    fun providesCarListUseCaseProvider(
        getSelectedCarsUseCase: GetSelectedCarsUseCase,
        deleteSelectedCarUseCase: DeleteSelectedCarUseCase,
        setCarAsMainUseCase: SetCarAsMainUseCase,
    ): CarListUseCaseProvider = CarListUseCaseProvider(
        getSelectedCarsUseCase,
        deleteSelectedCarUseCase,
        setCarAsMainUseCase
    )
}
