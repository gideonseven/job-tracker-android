package com.gt.jobtracker.feature.applications.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gt.jobtracker.core.domain.model.JobApplication
import com.gt.jobtracker.core.domain.repository.JobRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: JobRepository
) : ViewModel() {

    private var applicationId: Long = -1L

    lateinit var uiState: StateFlow<DetailUiState>

    fun loadApplication(id: Long) {
        applicationId = id
        uiState = repository.getApplicationById(id)
            .map { application ->
                if (application != null) {
                    DetailUiState.Success(application)
                } else {
                    DetailUiState.NotFound
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = DetailUiState.Loading
            )
    }

    fun deleteApplication(application: JobApplication, onDeleted: () -> Unit) {
        viewModelScope.launch {
            repository.deleteApplication(application)
            onDeleted()
        }
    }
}

sealed class DetailUiState {
    object Loading : DetailUiState()
    object NotFound : DetailUiState()
    data class Success(val application: JobApplication) : DetailUiState()
}