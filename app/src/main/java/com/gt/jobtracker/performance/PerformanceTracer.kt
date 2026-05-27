package com.gt.jobtracker.performance

import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PerformanceTracer @Inject constructor() {

    private val performance = FirebasePerformance.getInstance()
    private val activeTraces = mutableMapOf<String, Trace>()

    fun startTrace(name: String) {
        val trace = performance.newTrace(name)
        trace.start()
        activeTraces[name] = trace
    }

    fun stopTrace(name: String) {
        activeTraces[name]?.stop()
        activeTraces.remove(name)
    }

    fun addTraceAttribute(name: String, key: String, value: String) {
        activeTraces[name]?.putAttribute(key, value)
    }

    companion object {
        const val TRACE_APP_START = "app_start"
        const val TRACE_LOAD_APPLICATIONS = "load_applications"
        const val TRACE_SAVE_APPLICATION = "save_application"
    }
}