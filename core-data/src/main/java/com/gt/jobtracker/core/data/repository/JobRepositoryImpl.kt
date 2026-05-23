package com.gt.jobtracker.core.data.repository

import com.gt.jobtracker.core.data.local.JobApplicationDao
import com.gt.jobtracker.core.data.mapper.toDomain
import com.gt.jobtracker.core.data.mapper.toEntity
import com.gt.jobtracker.core.domain.model.JobApplication
import com.gt.jobtracker.core.domain.repository.JobRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JobRepositoryImpl @Inject constructor(
    private val dao: JobApplicationDao
) : JobRepository {

    override fun getAllApplications(): Flow<List<JobApplication>> {
        return dao.getAllApplications().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getApplicationById(id: Long): Flow<JobApplication?> {
        return dao.getApplicationById(id).map { it?.toDomain() }
    }

    override suspend fun upsertApplication(application: JobApplication) {
        dao.upsertApplication(application.toEntity())
    }

    override suspend fun deleteApplication(application: JobApplication) {
        dao.deleteApplication(application.toEntity())
    }
}