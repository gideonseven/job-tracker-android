package com.gt.jobtracker.feature.applications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.perf.FirebasePerformance
import com.gt.jobtracker.core.data.worker.SyncManager
import com.gt.jobtracker.core.domain.analytics.AnalyticsScreens
import com.gt.jobtracker.core.domain.analytics.JobAnalytics
import com.gt.jobtracker.core.domain.model.JobApplication
import com.gt.jobtracker.core.domain.model.JobStatus
import com.gt.jobtracker.core.domain.network.NetworkMonitor
import com.gt.jobtracker.core.domain.repository.JobRepository
import com.gt.jobtracker.core.domain.usecase.UpdateApplicationStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplicationsViewModel @Inject constructor(
    private val repository: JobRepository,
    private val updateStatusUseCase: UpdateApplicationStatusUseCase,
    private val analytics: JobAnalytics,
    private val syncManager: SyncManager,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val loadTrace = FirebasePerformance.getInstance()
        .newTrace("load_applications")
    private var traceCompleted = false

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()


    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
        .stateIn(
            scope        = viewModelScope,
            started      = SharingStarted.WhileSubscribed(5_000),
            initialValue = true    // default true to avoid false-offline flash on launch
        )

    init {
        analytics.trackScreen(AnalyticsScreens.APPLICATIONS_LIST)
        loadTrace.start()
    }

    val uiState: StateFlow<ApplicationsUiState> = repository
        .getAllApplications()
        .map { applications ->
            // Stop the trace only on the first emission — the trace can only be
            // stopped once; subsequent Room updates would otherwise throw
            // IllegalStateException and kill the flow via .catch {}.
            if (!traceCompleted) {
                loadTrace.stop()
                traceCompleted = true
            }
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
            result.onSuccess {
                analytics.trackStatusChanged(
                    company = application.company,
                    fromStatus = application.status.name,
                    toStatus = newStatus.name
                )
            }
            result.onFailure { error ->
                _event.update { error.message }
            }
        }
    }

    fun deleteApplication(application: JobApplication) {
        viewModelScope.launch {
            repository.deleteApplication(application)
            analytics.trackApplicationDeleted(application.company)
        }
    }

    fun eventConsumed() {
        _event.update { null }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            syncManager.enqueueSync()
            // Give WorkManager a moment to pick up and start the work
            // before hiding the spinner. The list will update reactively
            // via the Room Flow when SyncWorker writes new data.
            delay(1_000)
            _isRefreshing.value = false
        }
    }
}