package com.example.data.di

import com.example.data.repository.source.OpenCSVManager
import com.example.data.repository.source.OpenCSVManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

    @Binds
    @Singleton
    fun providesOpenCSVManager(
        impl: OpenCSVManagerImpl,
    ): OpenCSVManager
}
