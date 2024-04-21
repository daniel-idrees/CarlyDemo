package com.example.data.di

import com.example.data.repository.CarRepositoryImpl
import com.example.domain.repository.CarRepository
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
        impl: CarRepositoryImpl
    ): CarRepository
}
