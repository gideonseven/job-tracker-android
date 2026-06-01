package com.gt.jobtracker.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface JobApplicationDao {

    // --- existing methods (keep these) ---
    @Query("SELECT * FROM job_applications ORDER BY dateApplied DESC")
    fun getAllApplications(): Flow<List<JobApplicationEntity>>

    @Query("SELECT * FROM job_applications WHERE id = :id")
    fun getApplicationById(id: Long): Flow<JobApplicationEntity?>

    @Upsert
    suspend fun upsertApplication(application: JobApplicationEntity)

    // --- new: one-shot fetch by local Room id ---
    @Query("SELECT * FROM job_applications WHERE id = :id LIMIT 1")
    suspend fun getApplicationByIdOnce(id: Long): JobApplicationEntity?

    // --- new: all rows that have unsynced local changes ---
    @Query("SELECT * FROM job_applications WHERE pendingSync = 1")
    suspend fun getPendingSync(): List<JobApplicationEntity>

    // --- new: look up by server-assigned UUID ---
    @Query("SELECT * FROM job_applications WHERE serverId = :serverId LIMIT 1")
    suspend fun getApplicationByServerId(serverId: String): JobApplicationEntity?

    // --- new: physical delete (used only by SyncWorker after server confirms) ---
    @Delete
    suspend fun hardDelete(entity: JobApplicationEntity)
}