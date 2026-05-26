package com.gt.jobtracker.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.gt.jobtracker.core.data.notification.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val company = remoteMessage.data["company"] ?: return
        val roleTitle = remoteMessage.data["role_title"] ?: return
        val notificationId = remoteMessage.data["notification_id"]
            ?.toIntOrNull() ?: 0

        notificationHelper.showInterviewReminder(
            notificationId = notificationId,
            company = company,
            roleTitle = roleTitle
        )
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // token refresh — send to backend when available
    }
}