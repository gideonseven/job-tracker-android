package com.gt.jobtracker.core.data.mapper

import com.gt.jobtracker.core.data.local.JobApplicationEntity
import com.gt.jobtracker.core.domain.model.JobApplication

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