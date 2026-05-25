package com.gt.jobtracker.feature.applications

import com.gt.jobtracker.core.domain.model.JobApplication

sealed class ApplicationsUiState {
    object Loading : ApplicationsUiState()
    data class Success(val applications: List<JobApplication>) : ApplicationsUiState()
    data class Error(val message: String) : ApplicationsUiState()
}