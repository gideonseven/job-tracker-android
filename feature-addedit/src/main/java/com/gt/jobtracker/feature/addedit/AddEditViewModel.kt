package com.gt.jobtracker.feature.addedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gt.jobtracker.core.domain.analytics.AnalyticsScreens
import com.gt.jobtracker.core.domain.analytics.JobAnalytics
import com.gt.jobtracker.core.domain.model.JobApplication
import com.gt.jobtracker.core.domain.model.JobStatus
import com.gt.jobtracker.core.domain.repository.JobRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val repository: JobRepository,
    private val analytics: JobAnalytics
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditUiState())
    val uiState: StateFlow<AddEditUiState> = _uiState

    private val _saved = MutableStateFlow(false)
    val saved: StateFlow<Boolean> = _saved

    init {
        analytics.trackScreen(AnalyticsScreens.ADD_EDIT)
    }

    fun loadApplication(id: Long) {
        viewModelScope.launch {
            repository.getApplicationById(id).collect { application ->
                application?.let {
                    _uiState.update { state ->
                        state.copy(
                            company = it.company,
                            roleTitle = it.roleTitle,
                            status = it.status,
                            location = it.location ?: "",
                            jobUrl = it.jobUrl ?: "",
                            notes = it.notes ?: "",
                            isEditing = true,
                            existingId = it.id
                        )
                    }
                }
            }
        }
    }

    fun onCompanyChanged(value: String) {
        _uiState.update { it.copy(company = value) }
    }

    fun onRoleTitleChanged(value: String) {
        _uiState.update { it.copy(roleTitle = value) }
    }

    fun onStatusChanged(value: JobStatus) {
        _uiState.update { it.copy(status = value) }
    }

    fun onLocationChanged(value: String) {
        _uiState.update { it.copy(location = value) }
    }

    fun onJobUrlChanged(value: String) {
        _uiState.update { it.copy(jobUrl = value) }
    }

    fun onNotesChanged(value: String) {
        _uiState.update { it.copy(notes = value) }
    }

    fun save() {
        val state = _uiState.value
        if (state.company.isBlank() || state.roleTitle.isBlank()) {
            _uiState.update { it.copy(error = "Company and role title are required") }
            return
        }

        viewModelScope.launch {
            repository.upsertApplication(
                JobApplication(
                    id = state.existingId ?: 0L,
                    company = state.company.trim(),
                    roleTitle = state.roleTitle.trim(),
                    status = state.status,
                    dateApplied = System.currentTimeMillis(),
                    location = state.location.trim().ifBlank { null },
                    jobUrl = state.jobUrl.trim().ifBlank { null },
                    notes = state.notes.trim().ifBlank { null }
                )
            )
            val statusName = state.status.name
            if (state.isEditing) {
                analytics.trackApplicationUpdated(
                    company = state.company.trim(),
                    roleTitle = state.roleTitle.trim(),
                    status = statusName
                )
            } else {
                analytics.trackApplicationAdded(
                    company = state.company.trim(),
                    roleTitle = state.roleTitle.trim(),
                    status = statusName
                )
            }
            _saved.update { true }
        }
    }
}