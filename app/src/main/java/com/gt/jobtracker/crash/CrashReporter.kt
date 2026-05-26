package com.gt.jobtracker.crash

import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CrashReporter @Inject constructor() {

    private val crashlytics = FirebaseCrashlytics.getInstance()

    fun log(message: String) {
        crashlytics.log(message)
    }

    fun recordException(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }

    fun setUserId(userId: String) {
        crashlytics.setUserId(userId)
    }

    fun setCustomKey(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }
}