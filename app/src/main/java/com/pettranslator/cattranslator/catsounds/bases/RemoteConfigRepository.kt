package com.pettranslator.cattranslator.catsounds.bases

import android.content.Context
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.pettranslator.cattranslator.catsounds.model.AdConfig
import com.pettranslator.cattranslator.catsounds.model.AdsConfigParser
import com.pettranslator.cattranslator.catsounds.utils.ALog
import com.pettranslator.cattranslator.catsounds.utils.isInternetConnected
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class RemoteConfigRepository @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig, @ApplicationContext private val context: Context
) {
    suspend fun fetchAdsConfig(onSuccess: (AdConfig) -> Unit, onFailure: (Exception) -> Unit) {

        if (!isInternetConnected(context)) {
            ALog.d("AppContainer", "No network available, using default values")
            val allValues = remoteConfig.all
            ALog.d("AppContainer", "Default remote config values: $allValues")
            println(remoteConfig.all);
            try {
                val adConfig = AdsConfigParser.parseFromRemoteConfigValues(allValues)
                ALog.d("AppContainer", "All fetched local config values: $adConfig")
                onSuccess(adConfig)
            } catch (e: Exception) {
                onFailure(e)
            }
            return
        }
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            ALog.d("AppContainer", "Task: $task")
            if (task.isSuccessful) {

                val allValues = remoteConfig.all
                ALog.d("AppContainer", "All fetched remote config values: $allValues")
                try {
                    val adsConfig = AdsConfigParser.parseFromRemoteConfigValues(remoteConfig.all)
                    ALog.d("AppContainer", "AdsConfig: $adsConfig")
                    onSuccess(adsConfig)
                } catch (e: Exception) {
                    onFailure(e)
                }
            } else {
                onFailure(task.exception ?: Exception("Failed to fetch remote config"))
            }
        }
    }
}
