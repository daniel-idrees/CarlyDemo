package com.example.carlydemo.data.di

import com.example.carlydemo.data.repository.CarRepositoryImpl
import com.example.carlydemo.domain.repository.CarRepository
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
        impl: CarRepositoryImpl,
    ): CarRepository
}
