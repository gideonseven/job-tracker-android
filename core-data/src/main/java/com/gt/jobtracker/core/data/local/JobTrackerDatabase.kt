package com.gt.jobtracker.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.gt.jobtracker.core.domain.model.JobStatus

@Database(
    entities = [JobApplicationEntity::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(JobTrackerDatabase.Converters::class)
abstract class JobTrackerDatabase : RoomDatabase() {
    abstract fun jobApplicationDao(): JobApplicationDao

    class Converters {
        @TypeConverter
        fun fromJobStatus(status: JobStatus): String = status.name

        @TypeConverter
        fun toJobStatus(value: String): JobStatus = JobStatus.valueOf(value)
    }
}