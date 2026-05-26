package com.gt.jobtracker.core.data.notification

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val company = inputData.getString(KEY_COMPANY) ?: return Result.failure()
        val roleTitle = inputData.getString(KEY_ROLE_TITLE) ?: return Result.failure()
        val notificationId = inputData.getInt(KEY_NOTIFICATION_ID, 0)

        notificationHelper.showInterviewReminder(
            notificationId = notificationId,
            company = company,
            roleTitle = roleTitle
        )

        return Result.success()
    }

    companion object {
        const val KEY_COMPANY = "company"
        const val KEY_ROLE_TITLE = "role_title"
        const val KEY_NOTIFICATION_ID = "notification_id"
    }
}