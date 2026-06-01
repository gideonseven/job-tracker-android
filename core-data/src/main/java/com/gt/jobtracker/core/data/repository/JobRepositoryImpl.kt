package com.gt.jobtracker.core.data.repository

import com.gt.jobtracker.core.data.local.JobApplicationDao
import com.gt.jobtracker.core.data.mapper.toDomain
import com.gt.jobtracker.core.data.mapper.toEntity
import com.gt.jobtracker.core.data.network.JobTrackerApi
import com.gt.jobtracker.core.data.worker.SyncManager
import com.gt.jobtracker.core.domain.model.JobApplication
import com.gt.jobtracker.core.domain.repository.JobRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JobRepositoryImpl @Inject constructor(
    private val dao: JobApplicationDao,
    private val api: JobTrackerApi,
    private val syncManager: SyncManager
) : JobRepository {

    // READ — Room is the only source. API data arrives via SyncWorker.
    override fun getAllApplications(): Flow<List<JobApplication>> =
        dao.getAllApplications().map { entities ->
            entities
                .filter { it.deletedAt == null }   // hide soft-deleted records
                .map { it.toDomain() }
        }

    override fun getApplicationById(id: Long): Flow<JobApplication?> {
        return dao.getApplicationById(id).map { it?.toDomain() }
    }

    // WRITE — Room first, then attempt sync
    override suspend fun upsertApplication(application: JobApplication) {
        val entity = application.toEntity().copy(
            pendingSync = true,
            updatedAt = System.currentTimeMillis()
        )
        dao.upsertApplication(entity)
        syncManager.enqueueSync()   // WorkManager handles it when online
    }

    // SOFT DELETE — never hard-delete until server confirms
    override suspend fun deleteApplication(application: JobApplication) {
        val entity = dao.getApplicationByIdOnce(application.id) ?: return
        dao.upsertApplication(
            entity.copy(
                deletedAt = System.currentTimeMillis(),
                pendingSync = true
            )
        )
        syncManager.enqueueSync()
    }
}