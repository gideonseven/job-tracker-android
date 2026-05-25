package com.gt.jobtracker.core.domain.usecase

import com.gt.jobtracker.core.domain.model.JobApplication
import com.gt.jobtracker.core.domain.model.JobStatus
import com.gt.jobtracker.core.domain.repository.JobRepository
import javax.inject.Inject

class UpdateApplicationStatusUseCase @Inject constructor(
    private val repository: JobRepository
) {
    suspend operator fun invoke(
        application: JobApplication,
        newStatus: JobStatus
    ): Result<Unit> {
        if (!StatusTransitionManager.isValidTransition(application.status, newStatus)) {
            return Result.failure(
                IllegalStateException(
                    "Invalid transition: ${application.status} → $newStatus"
                )
            )
        }
        return try {
            repository.upsertApplication(application.copy(status = newStatus))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}