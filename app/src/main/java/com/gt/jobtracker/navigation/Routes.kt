package com.gt.jobtracker.navigation

import kotlinx.serialization.Serializable

@Serializable
object ApplicationsRoute

@Serializable
data class ApplicationDetailRoute(val applicationId: Long)

@Serializable
data class AddEditRoute(val applicationId: Long? = null)