package com.gt.jobtracker.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gt.jobtracker.core.domain.model.JobStatus

@Entity(tableName = "job_applications")
data class JobApplicationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val company: String,
    val roleTitle: String,
    val status: JobStatus,
    val dateApplied: Long,
    val jobUrl: String? = null,
    val notes: String? = null,
    val location: String? = null,
    val serverId: String? = null,
    val pendingSync: Boolean = true,
    val deletedAt: Long? = null,
    val updatedAt: Long = System.currentTimeMillis(),
    val syncError: String? = null
)