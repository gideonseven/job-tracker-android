package com.gt.jobtracker.core.domain.model

data class JobApplication(
    val id: Long = 0,
    val company: String,
    val roleTitle: String,
    val status: JobStatus,
    val dateApplied: Long,
    val jobUrl: String? = null,
    val notes: String? = null,
    val location: String? = null
)
