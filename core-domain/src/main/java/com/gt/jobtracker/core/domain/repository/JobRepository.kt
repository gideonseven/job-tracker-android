package com.gt.jobtracker.core.domain.repository

import com.gt.jobtracker.core.domain.model.JobApplication
import kotlinx.coroutines.flow.Flow

interface JobRepository {
    fun getAllApplications(): Flow<List<JobApplication>>
    fun getApplicationById(id: Long): Flow<JobApplication?>
    suspend fun upsertApplication(application: JobApplication)
    suspend fun deleteApplication(application: JobApplication)
}