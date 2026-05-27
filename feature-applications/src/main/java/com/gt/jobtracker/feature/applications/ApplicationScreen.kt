package com.gt.jobtracker.feature.applications

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gt.jobtracker.core.domain.model.JobApplication
import com.gt.jobtracker.core.domain.model.JobStatus
import com.gt.jobtracker.core.domain.usecase.StatusTransitionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationsScreen(
    onNavigateToDetail: (Long) -> Unit = {},
    onNavigateToAddEdit: () -> Unit = {},
    viewModel: ApplicationsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val event by viewModel.event.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(event) {
        event?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.eventConsumed()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Applications") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            androidx.compose.material3.FloatingActionButton(
                onClick = onNavigateToAddEdit
            ) {
                Text("+")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = uiState) {
                is ApplicationsUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is ApplicationsUiState.Success -> {
                    if (state.applications.isEmpty()) {
                        Text(
                            text = "No applications yet. Start tracking!",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn {
                            items(
                                items = state.applications,
                                key = { it.id }
                            ) { application ->
                                ApplicationItem(
                                    application = application,
                                    onStatusChange = { newStatus ->
                                        viewModel.updateStatus(
                                            application,
                                            newStatus
                                        )
                                    },
                                    onDelete = {
                                        viewModel.deleteApplication(application)
                                    },
                                    onTap = {
                                        onNavigateToDetail(application.id)
                                    }
                                )
                            }
                        }
                    }
                }

                is ApplicationsUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun ApplicationItem(
    application: JobApplication,
    onStatusChange: (com.gt.jobtracker.core.domain.model.JobStatus) -> Unit,
    onDelete: () -> Unit,
    onTap: () -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }
    val availableTransitions = StatusTransitionManager
        .getAvailableTransitions(application.status)

    Card(
        onClick = onTap,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = application.roleTitle,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = application.company,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = application.status.name.replace("_", " "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (availableTransitions.isNotEmpty()) {
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Text("▶")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        availableTransitions.forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status.name.replace("_", " ")) },
                                onClick = {
                                    onStatusChange(status)
                                    showMenu = false
                                }
                            )
                        }
                    }
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ApplicationsScreenPreview() {
    val sampleApplications = listOf(
        JobApplication(
            id = 1,
            company = "Atlassian",
            roleTitle = "Senior Android Engineer",
            status = JobStatus.PHONE_SCREEN,
            dateApplied = System.currentTimeMillis(),
            location = "Sydney, Australia"
        ),
        JobApplication(
            id = 2,
            company = "Canva",
            roleTitle = "Android Developer",
            status = JobStatus.APPLIED,
            dateApplied = System.currentTimeMillis(),
            location = "Remote"
        ),
        JobApplication(
            id = 3,
            company = "Afterpay",
            roleTitle = "Mobile Engineer",
            status = JobStatus.TECHNICAL,
            dateApplied = System.currentTimeMillis(),
            location = "Melbourne, Australia"
        )
    )

    MaterialTheme {
        LazyColumn {
            items(sampleApplications) { application ->
                ApplicationItem(
                    application = application,
                    onStatusChange = {},
                    onDelete = {}
                )
            }
        }
    }
}