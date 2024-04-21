package com.example.data.di

import com.example.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {

    @Singleton
    @Provides
    fun provideCarDao(db: AppDatabase) = db.carDao()
}
