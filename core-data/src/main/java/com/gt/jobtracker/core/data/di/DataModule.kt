package com.gt.jobtracker.core.data.di

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.room.Room
import androidx.work.WorkerFactory
import com.gt.jobtracker.core.data.local.JobApplicationDao
import com.gt.jobtracker.core.data.local.JobTrackerDatabase
import com.gt.jobtracker.core.data.network.ConnectivityNetworkMonitor
import com.gt.jobtracker.core.data.repository.JobRepositoryImpl
import com.gt.jobtracker.core.domain.network.NetworkMonitor
import com.gt.jobtracker.core.domain.repository.JobRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindJobRepository(
        impl: JobRepositoryImpl
    ): JobRepository

    @Binds
    abstract fun bindWorkerFactory(
        factory: HiltWorkerFactory
    ): WorkerFactory

    @Binds
    abstract fun bindNetworkMonitor(
        impl: ConnectivityNetworkMonitor
    ): NetworkMonitor

    companion object {
        @Provides
        @Singleton
        fun provideDatabase(
            @ApplicationContext context: Context
        ): JobTrackerDatabase {
            return Room.databaseBuilder(
                context,
                JobTrackerDatabase::class.java,
                "job_tracker.db"
            ).build()
        }

        @Provides
        @Singleton
        fun provideJobApplicationDao(
            database: JobTrackerDatabase
        ): JobApplicationDao {
            return database.jobApplicationDao()
        }
    }
}