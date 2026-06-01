package com.gt.jobtracker.core.data.mapper

import com.gt.jobtracker.core.data.local.JobApplicationEntity
import com.gt.jobtracker.core.data.remote.dto.ApplicationDto
import com.gt.jobtracker.core.domain.model.JobApplication
import com.gt.jobtracker.core.domain.model.JobStatus

fun JobApplicationEntity.toDomain(): JobApplication {
    return JobApplication(
        id = id,
        company = company,
        roleTitle = roleTitle,
        status = status,
        dateApplied = dateApplied,
        jobUrl = jobUrl,
        notes = notes,
        location = location
    )
}

fun JobApplication.toEntity(): JobApplicationEntity {
    return JobApplicationEntity(
        id = id,
        company = company,
        roleTitle = roleTitle,
        status = status,
        dateApplied = dateApplied,
        jobUrl = jobUrl,
        notes = notes,
        location = location
    )
}

fun ApplicationDto.toDomain(): JobApplication = JobApplication(
    id      = 0L,           // local Room id assigned on insert
    company = company,
    roleTitle = roleTitle,
    status  = runCatching { JobStatus.valueOf(status) }
        .getOrDefault(JobStatus.SAVED),  // unknown status → safe default
    dateApplied = dateApplied,
    jobUrl  = jobUrl,
    notes   = notes,
    location = location
)

fun JobApplicationEntity.toDto(): ApplicationDto = ApplicationDto(
    id          = serverId ?: "",
    company     = company,
    roleTitle   = roleTitle,
    status      = status.name,
    dateApplied = dateApplied,
    jobUrl      = jobUrl,
    notes       = notes,
    location    = location,
    updatedAt   = updatedAt
)

fun ApplicationDto.toEntity(): JobApplicationEntity = JobApplicationEntity(
    id          = 0L,           // Room assigns the local id on insert
    company     = company,
    roleTitle   = roleTitle,
    status      = runCatching { JobStatus.valueOf(status) }
        .getOrDefault(JobStatus.SAVED),
    dateApplied = dateApplied,
    jobUrl      = jobUrl,
    notes       = notes,
    location    = location,
    serverId    = id,           // the server's UUID
    pendingSync = false,        // just arrived from server, nothing to push
    deletedAt   = null,
    updatedAt   = updatedAt,
    syncError   = null
)