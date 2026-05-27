package com.gt.jobtracker.flags

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.gt.jobtracker.core.domain.flags.FeatureFlags
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeatureFlagManager @Inject constructor() {

    private val remoteConfig = FirebaseRemoteConfig.getInstance()

    init {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()

        remoteConfig.setConfigSettingsAsync(configSettings)

        // default values — all flags off by default
        remoteConfig.setDefaultsAsync(
            mapOf(
                FeatureFlags.DASHBOARD_STATISTICS to false,
                FeatureFlags.ADD_EDIT_NOTES to true,
                FeatureFlags.EXPORT_APPLICATIONS to false
            )
        )
    }

    fun isEnabled(flag: String): Boolean {
        return remoteConfig.getBoolean(flag)
    }

    fun fetch(onComplete: () -> Unit = {}) {
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            onComplete()
        }
    }
}