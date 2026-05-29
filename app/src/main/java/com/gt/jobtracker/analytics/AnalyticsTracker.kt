package com.gt.jobtracker.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.gt.jobtracker.core.domain.analytics.JobAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsTracker @Inject constructor(
    @ApplicationContext context: Context
) : JobAnalytics {

    private val analytics = FirebaseAnalytics.getInstance(context)

    override fun trackScreen(screenName: String) {
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        }
    }

    override fun trackApplicationAdded(company: String, roleTitle: String, status: String) {
        analytics.logEvent("application_added") {
            param("company", company)
            param("role_title", roleTitle)
            param("status", status)
        }
    }

    override fun trackApplicationUpdated(company: String, roleTitle: String, status: String) {
        analytics.logEvent("application_updated") {
            param("company", company)
            param("role_title", roleTitle)
            param("status", status)
        }
    }

    override fun trackStatusChanged(
        company: String,
        fromStatus: String,
        toStatus: String
    ) {
        analytics.logEvent("status_changed") {
            param("company", company)
            param("from_status", fromStatus)
            param("to_status", toStatus)
        }
    }

    override fun trackApplicationDeleted(company: String) {
        analytics.logEvent("application_deleted") {
            param("company", company)
        }
    }

    fun trackFeatureUsed(featureName: String) {
        analytics.logEvent("feature_used") {
            param("feature_name", featureName)
        }
    }
}