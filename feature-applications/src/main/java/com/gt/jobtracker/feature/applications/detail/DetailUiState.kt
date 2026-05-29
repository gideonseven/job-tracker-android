package com.gt.jobtracker.feature.applications.detail

import com.gt.jobtracker.core.domain.model.JobApplication

sealed class DetailUiState {
    object Loading : DetailUiState()
    object NotFound : DetailUiState()
    data class Error(val message: String) : DetailUiState()
    data class Success(val application: JobApplication) : DetailUiState()
}
