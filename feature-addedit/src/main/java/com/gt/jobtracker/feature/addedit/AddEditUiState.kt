package com.gt.jobtracker.feature.addedit

import com.gt.jobtracker.core.domain.model.JobStatus

data class AddEditUiState(
    val company: String = "",
    val roleTitle: String = "",
    val status: JobStatus = JobStatus.SAVED,
    val location: String = "",
    val jobUrl: String = "",
    val notes: String = "",
    val error: String? = null,
    val isEditing: Boolean = false,
    val existingId: Long? = null
)