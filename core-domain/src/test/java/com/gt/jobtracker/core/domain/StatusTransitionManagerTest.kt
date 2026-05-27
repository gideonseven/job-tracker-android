package com.gt.jobtracker.core.domain

import com.gt.jobtracker.core.domain.model.JobStatus
import com.gt.jobtracker.core.domain.usecase.StatusTransitionManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StatusTransitionManagerTest {

    @Test
    fun `SAVED can only transition to APPLIED`() {
        val transitions = StatusTransitionManager
            .getAvailableTransitions(JobStatus.SAVED)
        assertEquals(listOf(JobStatus.APPLIED), transitions)
    }

    @Test
    fun `APPLIED can transition to PHONE_SCREEN, REJECTED, GHOSTED`() {
        val transitions = StatusTransitionManager
            .getAvailableTransitions(JobStatus.APPLIED)
        assertEquals(
            listOf(
                JobStatus.PHONE_SCREEN,
                JobStatus.REJECTED,
                JobStatus.GHOSTED
            ),
            transitions
        )
    }

    @Test
    fun `OFFER is terminal - no transitions available`() {
        val transitions = StatusTransitionManager
            .getAvailableTransitions(JobStatus.OFFER)
        assertTrue(transitions.isEmpty())
    }

    @Test
    fun `REJECTED is terminal - no transitions available`() {
        val transitions = StatusTransitionManager
            .getAvailableTransitions(JobStatus.REJECTED)
        assertTrue(transitions.isEmpty())
    }

    @Test
    fun `GHOSTED can transition back to APPLIED`() {
        val transitions = StatusTransitionManager
            .getAvailableTransitions(JobStatus.GHOSTED)
        assertEquals(listOf(JobStatus.APPLIED), transitions)
    }

    @Test
    fun `valid transition returns true`() {
        assertTrue(
            StatusTransitionManager.isValidTransition(
                JobStatus.APPLIED,
                JobStatus.PHONE_SCREEN
            )
        )
    }

    @Test
    fun `invalid transition returns false`() {
        assertFalse(
            StatusTransitionManager.isValidTransition(
                JobStatus.REJECTED,
                JobStatus.OFFER
            )
        )
    }

    @Test
    fun `cannot skip stages - SAVED to TECHNICAL is invalid`() {
        assertFalse(
            StatusTransitionManager.isValidTransition(
                JobStatus.SAVED,
                JobStatus.TECHNICAL
            )
        )
    }
}