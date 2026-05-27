package com.gt.jobtracker.feature.applications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.perf.FirebasePerformance
import com.gt.jobtracker.core.domain.model.JobApplication
import com.gt.jobtracker.core.domain.model.JobStatus
import com.gt.jobtracker.core.domain.repository.JobRepository
import com.gt.jobtracker.core.domain.usecase.UpdateApplicationStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplicationsViewModel @Inject constructor(
    private val repository: JobRepository,
    private val updateStatusUseCase: UpdateApplicationStatusUseCase
) : ViewModel() {

    private val loadTrace = FirebasePerformance.getInstance()
        .newTrace("load_applications")

    init {
        loadTrace.start()
    }

    val uiState: StateFlow<ApplicationsUiState> = repository
        .getAllApplications()
        .map { applications ->
            loadTrace.stop()
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

    private val _event = MutableStateFlow<String?>(null)
    val event: StateFlow<String?> = _event

    fun updateStatus(application: JobApplication, newStatus: JobStatus) {
        viewModelScope.launch {
            val result = updateStatusUseCase(application, newStatus)
            result.onFailure { error ->
                _event.update { error.message }
            }
        }
    }

    fun deleteApplication(application: JobApplication) {
        viewModelScope.launch {
            repository.deleteApplication(application)
        }
    }

    fun eventConsumed() {
        _event.update { null }
    }
}