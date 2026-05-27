package com.gt.jobtracker.performance

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.perf.FirebasePerformance

class PerformanceInitializer : Initializer<FirebasePerformance> {

    override fun create(context: Context): FirebasePerformance {
        return FirebasePerformance.getInstance().also {
            it.isPerformanceCollectionEnabled = true
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}