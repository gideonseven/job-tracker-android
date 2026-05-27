package com.gt.jobtracker.di

import com.gt.jobtracker.flags.FeatureFlagManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFeatureFlagManager(): FeatureFlagManager {
        return FeatureFlagManager()
    }
}