package com.gt.jobtracker.feature.applications.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    applicationId: Long,
    onNavigateBack: () -> Unit = {},
    onNavigateToEdit: (Long) -> Unit = {},
    viewModel: DetailViewModel = hiltViewModel()
) {
    LaunchedEffect(applicationId) {
        viewModel.loadApplication(applicationId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Application Detail") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    when (val state = uiState) {
                        is DetailUiState.Success -> {
                            IconButton(onClick = {
                                onNavigateToEdit(state.application.id)
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit")
                            }
                            IconButton(onClick = {
                                viewModel.deleteApplication(state.application) {
                                    onNavigateBack()
                                }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                        else -> {}
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = uiState) {
                is DetailUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is DetailUiState.NotFound -> {
                    Text(
                        text = "Application not found.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is DetailUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DetailCard(
                            label = "Role Title",
                            value = state.application.roleTitle
                        )
                        DetailCard(
                            label = "Company",
                            value = state.application.company
                        )
                        DetailCard(
                            label = "Status",
                            value = state.application.status.name.replace("_", " ")
                        )
                        state.application.location?.let {
                            DetailCard(label = "Location", value = it)
                        }
                        state.application.jobUrl?.let {
                            DetailCard(label = "Job URL", value = it)
                        }
                        state.application.notes?.let {
                            DetailCard(label = "Notes", value = it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailCard(label: String, value: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}