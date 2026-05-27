package com.gt.jobtracker.feature.applications.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gt.jobtracker.core.domain.model.JobApplication
import com.gt.jobtracker.core.domain.model.JobStatus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        val successState = uiState as? DetailUiState.Success
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Application") },
            text = {
                val detail = successState?.let {
                    " for ${it.application.roleTitle} at ${it.application.company}"
                } ?: ""
                Text("Are you sure you want to delete this application$detail? This cannot be undone.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        successState?.let {
                            viewModel.deleteApplication(it.application) { onNavigateBack() }
                        }
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Application Detail") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (uiState is DetailUiState.Success) {
                        val state = uiState as DetailUiState.Success
                        IconButton(onClick = { onNavigateToEdit(state.application.id) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
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
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is DetailUiState.NotFound -> {
                    Text(
                        text = "Application not found.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is DetailUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Button(onClick = { viewModel.retry(applicationId) }) {
                            Text("Retry")
                        }
                    }
                }
                is DetailUiState.Success -> {
                    DetailContent(application = state.application)
                }
            }
        }
    }
}

@Composable
private fun DetailContent(application: JobApplication) {
    val uriHandler = LocalUriHandler.current
    val dateFormatter = remember { SimpleDateFormat("MMM d, yyyy", Locale.getDefault()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DetailCard(label = "Role Title", value = application.roleTitle)
        DetailCard(label = "Company", value = application.company)

        // Status with colored chip
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Status",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                StatusChip(
                    status = application.status,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Date Applied
        DetailCard(
            label = "Date Applied",
            value = dateFormatter.format(Date(application.dateApplied))
        )

        application.location?.let {
            DetailCard(label = "Location", value = it)
        }

        // Clickable Job URL
        application.jobUrl?.let { url ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Job URL",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = url,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .clickable { uriHandler.openUri(url) }
                    )
                }
            }
        }

        application.notes?.let {
            DetailCard(label = "Notes", value = it)
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

@Composable
fun StatusChip(status: JobStatus, modifier: Modifier = Modifier) {
    val (containerColor, labelColor) = when (status) {
        JobStatus.SAVED        -> Color(0xFFE0E0E0) to Color(0xFF424242)
        JobStatus.APPLIED      -> Color(0xFFBBDEFB) to Color(0xFF0D47A1)
        JobStatus.PHONE_SCREEN -> Color(0xFFB2EBF2) to Color(0xFF006064)
        JobStatus.TECHNICAL    -> Color(0xFFFFE0B2) to Color(0xFFE65100)
        JobStatus.FINAL_ROUND  -> Color(0xFFE1BEE7) to Color(0xFF4A148C)
        JobStatus.OFFER        -> Color(0xFFC8E6C9) to Color(0xFF1B5E20)
        JobStatus.REJECTED     -> Color(0xFFFFCDD2) to Color(0xFFB71C1C)
        JobStatus.GHOSTED      -> Color(0xFFCFD8DC) to Color(0xFF37474F)
    }
    Surface(
        color = containerColor,
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        Text(
            text = status.name.replace("_", " "),
            color = labelColor,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailContentPreview() {
    val fakeApplication = JobApplication(
        id = 1L,
        company = "Google",
        roleTitle = "Senior Android Engineer",
        status = JobStatus.TECHNICAL,
        dateApplied = System.currentTimeMillis(),
        location = "New York, NY",
        jobUrl = "https://careers.google.com/jobs/results/1234",
        notes = "Referred by Jane Doe. Strong culture fit — follow up after onsite."
    )
    DetailContent(application = fakeApplication)
}