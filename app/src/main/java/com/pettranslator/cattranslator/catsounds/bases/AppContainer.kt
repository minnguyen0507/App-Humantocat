package com.pettranslator.cattranslator.catsounds.bases

import com.pettranslator.cattranslator.catsounds.model.AdConfig
import com.pettranslator.cattranslator.catsounds.utils.ALog
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class AppContainer @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository
) {
    var adConfig: AdConfig? = null
        private set

    suspend fun initializeAdConfig(onComplete: (Boolean) -> Unit) {
        remoteConfigRepository.fetchAdsConfig (
            onSuccess = { config ->
                adConfig = config
                onComplete(true) // Fetch thành công
            },
            onFailure = { exception ->
                ALog.d("AppContainer", "Failed to fetch config: ${exception.message}")
                onComplete(false) // Fetch thất bại
            }
        )
    }


}