package com.gt.jobtracker.core.data.remote

import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val code: Int, val message: String) : NetworkResult<Nothing>()
    data object Loading : NetworkResult<Nothing>()
}

suspend fun <T> safeApiCall(
    call: suspend () -> T
): NetworkResult<T> {
    return try {
        NetworkResult.Success(call())
    } catch (e: HttpException) {
        // Server responded but with an error code (4xx, 5xx)
        Timber.w(e, "HTTP error ${e.code()}")
        NetworkResult.Error(e.code(), e.message() ?: "HTTP error")
    } catch (e: IOException) {
        // No response at all — no internet, timeout, DNS failure
        Timber.w(e, "Network IO error")
        NetworkResult.Error(-1, "No internet connection")
    }
}