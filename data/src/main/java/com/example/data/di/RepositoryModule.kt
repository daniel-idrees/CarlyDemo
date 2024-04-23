package com.example.data.di

import com.example.data.repository.SelectedCarRepository
import com.example.data.repository.SelectedCarDbRepository
import com.example.data.repository.CarRepository
import com.example.data.repository.CarCSVRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {

    @Binds
    @Singleton
    fun providesCarRepository(
        impl: CarCSVRepository
    ): CarRepository

    @Binds
    @Singleton
    fun providesCarDbRepository(
        impl: SelectedCarDbRepository
    ): SelectedCarRepository
}
