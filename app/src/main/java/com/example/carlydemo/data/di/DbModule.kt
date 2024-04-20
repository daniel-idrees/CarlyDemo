package com.example.carlydemo.data.di

import android.content.Context
import androidx.room.Room
import com.example.carlydemo.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DbModule {
    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "car_database"
    ).build()

    @Singleton
    @Provides
    fun provideCarDao(db: AppDatabase) = db.carDao()
}