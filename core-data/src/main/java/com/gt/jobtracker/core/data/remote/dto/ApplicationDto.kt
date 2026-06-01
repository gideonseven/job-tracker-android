package com.gt.jobtracker.core.data.remote.dto

data class ApplicationDto(
    val id: String,             // server UUID
    val company: String,
    val roleTitle: String,
    val status: String,         // plain String, not JobStatus enum
    val dateApplied: Long,
    val jobUrl: String?,
    val notes: String?,
    val location: String?,
    val updatedAt: Long
)