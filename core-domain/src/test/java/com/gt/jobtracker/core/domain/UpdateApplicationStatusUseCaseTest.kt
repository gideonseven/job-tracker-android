package com.gt.jobtracker.core.domain

import com.gt.jobtracker.core.domain.model.JobApplication
import com.gt.jobtracker.core.domain.model.JobStatus
import com.gt.jobtracker.core.domain.repository.JobRepository
import com.gt.jobtracker.core.domain.usecase.UpdateApplicationStatusUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UpdateApplicationStatusUseCaseTest {

    private lateinit var repository: JobRepository
    private lateinit var useCase: UpdateApplicationStatusUseCase

    private val sampleApplication = JobApplication(
        id = 1L,
        company = "Atlassian",
        roleTitle = "Senior Android Engineer",
        status = JobStatus.APPLIED,
        dateApplied = System.currentTimeMillis()
    )

    @Before
    fun setup() {
        repository = mockk()
        useCase = UpdateApplicationStatusUseCase(repository)
    }

    @Test
    fun `valid transition saves updated application`() = runTest {
        coEvery { repository.upsertApplication(any()) } returns Unit

        val result = useCase(sampleApplication, JobStatus.PHONE_SCREEN)

        assertTrue(result.isSuccess)
        coVerify {
            repository.upsertApplication(
                sampleApplication.copy(status = JobStatus.PHONE_SCREEN)
            )
        }
    }

    @Test
    fun `invalid transition returns failure`() = runTest {
        val result = useCase(sampleApplication, JobStatus.OFFER)

        assertTrue(result.isFailure)
        coVerify(exactly = 0) { repository.upsertApplication(any()) }
    }

    @Test
    fun `repository exception returns failure`() = runTest {
        coEvery { repository.upsertApplication(any()) } throws Exception("DB error")

        val result = useCase(sampleApplication, JobStatus.PHONE_SCREEN)

        assertTrue(result.isFailure)
    }
}