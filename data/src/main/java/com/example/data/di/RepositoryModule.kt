package com.example.data.di

import androidx.room.Dao
import com.example.data.database.dao.CarDao
import com.example.data.repository.CarRepositoryImpl
import com.example.data.repository.source.OpenCSVManager
import com.example.domain.repository.CarRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class RepositoryModule {

    @Singleton
    @Provides
    fun providesCarRepository(
        openCSVManager: OpenCSVManager,
        dao: CarDao
    ): CarRepository = CarRepositoryImpl(openCSVManager, dao)
}
