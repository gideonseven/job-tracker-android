package com.gt.jobtracker.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface JobApplicationDao {
    @Query("SELECT * FROM job_applications ORDER BY dateApplied DESC")
    fun getAllApplications(): Flow<List<JobApplicationEntity>>

    @Query("SELECT * FROM job_applications WHERE id = :id")
    fun getApplicationById(id: Long): Flow<JobApplicationEntity?>

    @Upsert
    suspend fun upsertApplication(application: JobApplicationEntity)

    @Delete
    suspend fun deleteApplication(application: JobApplicationEntity)
}