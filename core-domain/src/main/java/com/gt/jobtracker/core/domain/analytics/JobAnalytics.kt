package com.gt.jobtracker.core.domain.analytics

/**
 * Abstraction for logging user interactions. Implemented in the app module
 * (e.g. Firebase Analytics) so feature modules stay free of SDK dependencies.
 */
interface JobAnalytics {
    fun trackScreen(screenName: String)
    fun trackApplicationAdded(company: String, roleTitle: String, status: String)
    fun trackApplicationUpdated(company: String, roleTitle: String, status: String)
    fun trackStatusChanged(company: String, fromStatus: String, toStatus: String)
    fun trackApplicationDeleted(company: String)
}

object AnalyticsScreens {
    const val APPLICATIONS_LIST = "applications_list"
    const val APPLICATION_DETAIL = "application_detail"
    const val ADD_EDIT = "add_edit"
}
