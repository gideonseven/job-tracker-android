package com.gt.jobtracker.feature.applications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gt.jobtracker.core.domain.repository.JobRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ApplicationsViewModel @Inject constructor(
    private val repository: JobRepository
) : ViewModel() {
    val uiState: StateFlow<ApplicationsUiState> = repository
        .getAllApplications()
        .map { applications ->
            ApplicationsUiState.Success(applications) as ApplicationsUiState
        }
        .catch { throwable ->
            emit(ApplicationsUiState.Error(throwable.message ?: "Unknown error"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ApplicationsUiState.Loading
        )
}