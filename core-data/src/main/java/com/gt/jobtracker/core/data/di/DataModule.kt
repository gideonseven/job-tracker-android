package com.gt.jobtracker.core.data.di

import android.content.Context
import androidx.room.Room
import com.gt.jobtracker.core.data.local.JobApplicationDao
import com.gt.jobtracker.core.data.local.JobTrackerDatabase
import com.gt.jobtracker.core.data.repository.JobRepositoryImpl
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