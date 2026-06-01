package com.gt.jobtracker.core.data.network

import com.gt.jobtracker.core.data.remote.dto.ApplicationDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface JobTrackerApi {

    @GET("applications")
    suspend fun getApplications(): List<ApplicationDto>

    @POST("applications")
    suspend fun createApplication(@Body dto: ApplicationDto): ApplicationDto
    // Returns the saved DTO with the server-assigned `id`

    @PUT("applications/{id}")
    suspend fun updateApplication(
        @Path("id") serverId: String,
        @Body dto: ApplicationDto
    ): ApplicationDto

    @DELETE("applications/{id}")
    suspend fun deleteApplication(@Path("id") serverId: String)
    // Returns nothing — 204 No Content
}