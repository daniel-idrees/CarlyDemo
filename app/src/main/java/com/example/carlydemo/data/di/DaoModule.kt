package com.example.carlydemo.data.di

import com.example.carlydemo.data.database.AppDatabase
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
