package com.gt.jobtracker.core.data.notification

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.gt.jobtracker.core.domain.model.JobApplication
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun scheduleReminder(
        application: JobApplication,
        delayHours: Long = 24
    ) {
        val inputData = Data.Builder()
            .putString(ReminderWorker.KEY_COMPANY, application.company)
            .putString(ReminderWorker.KEY_ROLE_TITLE, application.roleTitle)
            .putInt(ReminderWorker.KEY_NOTIFICATION_ID, application.id.toInt())
            .build()

        val reminderRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInputData(inputData)
            .setInitialDelay(delayHours, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(context).enqueue(reminderRequest)
    }

    fun scheduleImmediateReminder(application: JobApplication) {
        scheduleReminder(application, delayHours = 0)
    }

    fun cancelReminder(applicationId: Long) {
        WorkManager.getInstance(context)
            .cancelAllWorkByTag("reminder_$applicationId")
    }
}