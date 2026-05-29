package com.gt.jobtracker.feature.applications.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gt.jobtracker.core.domain.model.JobApplication
import com.gt.jobtracker.core.domain.repository.JobRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: JobRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    fun loadApplication(id: Long) {
        loadJob?.cancel()
        _uiState.value = DetailUiState.Loading
        loadJob = viewModelScope.launch {
            repository.getApplicationById(id)
                .catch { e ->
                    _uiState.value = DetailUiState.Error(
                        e.message ?: "Failed to load application"
                    )
                }
                .map { application ->
                    if (application != null) DetailUiState.Success(application)
                    else DetailUiState.NotFound
                }
                .collect { _uiState.value = it }
        }
    }

    fun retry(id: Long) {
        loadApplication(id)
    }

    fun deleteApplication(application: JobApplication, onDeleted: () -> Unit) {
        viewModelScope.launch {
            repository.deleteApplication(application)
            onDeleted()
        }
    }
}