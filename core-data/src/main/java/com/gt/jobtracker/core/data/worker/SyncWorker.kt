package com.gt.jobtracker.core.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gt.jobtracker.core.data.local.JobApplicationDao
import com.gt.jobtracker.core.data.mapper.toDto
import com.gt.jobtracker.core.data.mapper.toEntity
import com.gt.jobtracker.core.data.network.JobTrackerApi
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.io.IOException

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val dao: JobApplicationDao,
    private val api: JobTrackerApi
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            pushPendingChanges()
            pullRemoteChanges()
            Result.success()
        } catch (e: IOException) {
            Timber.w(e, "SyncWorker: network unavailable, will retry")
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        } catch (e: Exception) {
            Timber.e(e, "SyncWorker: unexpected error")
            Result.failure()
        }
    }

    private suspend fun pushPendingChanges() {
        val pending = dao.getPendingSync()

        for (entity in pending) {
            when {
                // Soft-deleted and already on the server — tell server, then hard-delete
                entity.deletedAt != null && entity.serverId != null -> {
                    api.deleteApplication(entity.serverId)
                    dao.hardDelete(entity)
                }

                // Soft-deleted but never reached the server — just remove locally
                entity.deletedAt != null && entity.serverId == null -> {
                    dao.hardDelete(entity)
                }

                // New record — POST and save the server-assigned id back to Room
                entity.serverId == null -> {
                    val response = api.createApplication(entity.toDto())
                    dao.upsertApplication(
                        entity.copy(
                            serverId = response.id,
                            pendingSync = false,
                            syncError = null
                        )
                    )
                }

                // Existing record with local changes — PUT
                else -> {
                    api.updateApplication(entity.serverId, entity.toDto())
                    dao.upsertApplication(
                        entity.copy(
                            pendingSync = false,
                            syncError = null
                        )
                    )
                }
            }
        }
    }

    private suspend fun pullRemoteChanges() {
        val remoteList = api.getApplications()

        for (dto in remoteList) {
            val local = dao.getApplicationByServerId(dto.id)

            when {
                // Record exists on the server but not locally — insert it
                local == null -> {
                    dao.upsertApplication(dto.toEntity())
                }
                // Server version is newer and local has no unsaved changes — overwrite
                dto.updatedAt > local.updatedAt && !local.pendingSync -> {
                    dao.upsertApplication(dto.toEntity().copy(id = local.id))
                }
                // local.pendingSync == true means the user has unsaved local edits —
                // do not overwrite; our push already queued those changes
            }
        }
    }

    companion object {
        const val WORK_NAME = "sync_worker"
    }
}