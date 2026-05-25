package com.gt.jobtracker.core.domain.usecase

import com.gt.jobtracker.core.domain.model.JobStatus

object StatusTransitionManager {

    private val validTransitions = mapOf(
        JobStatus.SAVED to listOf(
            JobStatus.APPLIED
        ),
        JobStatus.APPLIED to listOf(
            JobStatus.PHONE_SCREEN,
            JobStatus.REJECTED,
            JobStatus.GHOSTED
        ),
        JobStatus.PHONE_SCREEN to listOf(
            JobStatus.TECHNICAL,
            JobStatus.REJECTED,
            JobStatus.GHOSTED
        ),
        JobStatus.TECHNICAL to listOf(
            JobStatus.FINAL_ROUND,
            JobStatus.REJECTED,
            JobStatus.GHOSTED
        ),
        JobStatus.FINAL_ROUND to listOf(
            JobStatus.OFFER,
            JobStatus.REJECTED,
            JobStatus.GHOSTED
        ),
        JobStatus.OFFER to emptyList(),
        JobStatus.REJECTED to emptyList(),
        JobStatus.GHOSTED to listOf(
            JobStatus.APPLIED
        )
    )

    fun getAvailableTransitions(current: JobStatus): List<JobStatus> {
        return validTransitions[current] ?: emptyList()
    }

    fun isValidTransition(from: JobStatus, to: JobStatus): Boolean {
        return validTransitions[from]?.contains(to) ?: false
    }
}