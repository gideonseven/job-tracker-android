package com.gt.jobtracker.di

import com.gt.jobtracker.analytics.AnalyticsTracker
import com.gt.jobtracker.core.domain.analytics.JobAnalytics
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    @Binds
    @Singleton
    abstract fun bindJobAnalytics(impl: AnalyticsTracker): JobAnalytics
}
