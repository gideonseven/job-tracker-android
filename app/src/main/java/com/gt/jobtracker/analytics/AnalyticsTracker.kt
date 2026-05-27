package com.gt.jobtracker.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsTracker @Inject constructor(
    @ApplicationContext context: Context
) {
    private val analytics = FirebaseAnalytics.getInstance(context)

    // screen tracking
    fun trackScreen(screenName: String) {
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        }
    }

    // application events
    fun trackApplicationAdded(company: String, roleTitle: String) {
        analytics.logEvent("application_added") {
            param("company", company)
            param("role_title", roleTitle)
        }
    }

    fun trackStatusChanged(
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

    fun trackApplicationDeleted(company: String) {
        analytics.logEvent("application_deleted") {
            param("company", company)
        }
    }

    // feature usage
    fun trackFeatureUsed(featureName: String) {
        analytics.logEvent("feature_used") {
            param("feature_name", featureName)
        }
    }

    companion object {
        const val SCREEN_APPLICATIONS = "applications_list"
        const val SCREEN_DETAIL = "application_detail"
        const val SCREEN_ADD_EDIT = "add_edit"
        const val SCREEN_DASHBOARD = "dashboard"
    }
}