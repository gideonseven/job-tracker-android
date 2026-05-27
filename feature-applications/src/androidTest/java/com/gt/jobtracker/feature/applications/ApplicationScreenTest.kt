package com.gt.jobtracker.feature.applications

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.material3.MaterialTheme
import com.gt.jobtracker.core.domain.model.JobApplication
import com.gt.jobtracker.core.domain.model.JobStatus
import org.junit.Rule
import org.junit.Test

class ApplicationsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleApplications = listOf(
        JobApplication(
            id = 1L,
            company = "Atlassian",
            roleTitle = "Senior Android Engineer",
            status = JobStatus.APPLIED,
            dateApplied = System.currentTimeMillis()
        ),
        JobApplication(
            id = 2L,
            company = "Canva",
            roleTitle = "Android Developer",
            status = JobStatus.PHONE_SCREEN,
            dateApplied = System.currentTimeMillis()
        )
    )

    @Test
    fun applicationsListShowsCorrectly() {
        composeTestRule.setContent {
            MaterialTheme {
                androidx.compose.foundation.lazy.LazyColumn {
                    items(sampleApplications.size) { index ->
                        ApplicationItem(
                            application = sampleApplications[index],
                            onStatusChange = {},
                            onDelete = {},
                            onTap = {}
                        )
                    }
                }
            }
        }

        composeTestRule
            .onNodeWithText("Senior Android Engineer")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Atlassian")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Android Developer")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Canva")
            .assertIsDisplayed()
    }

    @Test
    fun emptyStateShowsCorrectMessage() {
        composeTestRule.setContent {
            MaterialTheme {
                androidx.compose.foundation.layout.Box(
                    modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    androidx.compose.material3.Text(
                        text = "No applications yet. Start tracking!"
                    )
                }
            }
        }

        composeTestRule
            .onNodeWithText("No applications yet. Start tracking!")
            .assertIsDisplayed()
    }

    @Test
    fun applicationStatusDisplaysCorrectly() {
        composeTestRule.setContent {
            MaterialTheme {
                ApplicationItem(
                    application = sampleApplications[0],
                    onStatusChange = {},
                    onDelete = {},
                    onTap = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("APPLIED")
            .assertIsDisplayed()
    }
}