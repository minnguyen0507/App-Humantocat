package com.pettranslator.cattranslator.catsounds.utils

import android.os.Bundle
import com.google.android.gms.ads.AdValue
import com.google.firebase.analytics.FirebaseAnalytics
import com.pettranslator.cattranslator.catsounds.model.AnalyticsEvent


class AnalyticsHelper(private val firebaseAnalytics: FirebaseAnalytics) {

    // Phương thức chung để gửi sự kiện
    private fun logEvent(event: AnalyticsEvent) {
        firebaseAnalytics.logEvent(event.name, event.parameters)
    }

    // 1. Screen View
    fun logScreenView(screenName: String) {
        val params = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        }
        logEvent(AnalyticsEvent(FirebaseAnalytics.Event.SCREEN_VIEW, params))
    }

    // 2. Tutorial Begin
    fun logTutorialBegin(time: String, clicked: Boolean) {
        val params = Bundle().apply {
            putString("time", time)
            putBoolean("clicked", clicked)
        }
        logEvent(AnalyticsEvent(FirebaseAnalytics.Event.TUTORIAL_BEGIN, params))
    }

    // 3. Tutorial Complete
    fun logTutorialComplete(time: String, clicked: Boolean) {
        val params = Bundle().apply {
            putString("time", time)
            putBoolean("clicked", clicked)
        }
        logEvent(AnalyticsEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE, params))
    }

    // 4. App Update
    fun logAppUpdate(version: String) {
        val params = Bundle().apply {
            putString("version_name", version)
        }
        logEvent(AnalyticsEvent("update_app", params))
    }

    // 6. Notification Receive
    fun logNotificationReceive(notificationType: String) {
        val params = Bundle().apply {
            putString("notification_type", notificationType)
        }
        logEvent(AnalyticsEvent("receive_notification", params))
    }

    // 7. Notification Open
    fun logNotificationOpen(notificationType: String) {
        val params = Bundle().apply {
            putString("notification_type", notificationType)
        }
        logEvent(AnalyticsEvent("open_notification", params))
    }

    // 8. Language Select
    fun logLanguageSelect(language: String) {
        val params = Bundle().apply {
            putString("language_code", language)
        }
        logEvent(AnalyticsEvent("language_select", params))
    }

    // 9. Translate Cats
    fun logTranslateCats() {
        logEvent(AnalyticsEvent("translate_cats", Bundle()))
    }

    // 10. Translate Dogs
    fun logTranslateDogs() {
        logEvent(AnalyticsEvent("translate_dogs", Bundle()))
    }

    // 11. Play Sounds
    fun logPlaySounds(voiceType: String) {
        val params = Bundle().apply {
            putString("voice_type", voiceType)
        }
        logEvent(AnalyticsEvent("play_sounds", params))
    }

    // 12. Show Interstitial Ad
    fun logShowInterstitial(screenName: String) {
        val params = Bundle().apply {
            putString("screen_name", screenName)
        }
        logEvent(AnalyticsEvent("show_inter_position", params))
    }

    // 13. Show Banner Ad
    fun logShowBanner(screenName: String) {
        val params = Bundle().apply {
            putString("screen_name", screenName)
        }
        logEvent(AnalyticsEvent("show_banner_position", params))
    }

    // 14. Show Native Ad
    fun logShowNative(screenName: String) {
        val params = Bundle().apply {
            putString("screen_name", screenName)
        }
        logEvent(AnalyticsEvent("show_native_position", params))
    }

    // 15. Show Interstitial Ad Failed
    fun logShowInterstitialFailed(screenName: String) {
        val params = Bundle().apply {
            putString("screen_name", screenName)
        }
        logEvent(AnalyticsEvent("show_inter_failed_position", params))
    }

    // 16. Show Banner Ad Failed
    fun logShowBannerFailed(screenName: String) {
        val params = Bundle().apply {
            putString("screen_name", screenName)
        }
        logEvent(AnalyticsEvent("show_banner_failed_position", params))
    }

    // 17. Show Native Ad Failed
    fun logShowNativeFailed(screenName: String) {
        val params = Bundle().apply {
            putString("screen_name", screenName)
        }
        logEvent(AnalyticsEvent("show_native_failed_position", params))
    }

    fun logAdImpression(adType: String, adUnitId: String) {
        val params = Bundle().apply {
            putString("ad_type", adType)
            putString("ad_unit_id", adUnitId)
        }
        logEvent(AnalyticsEvent("ad_impression", params))
    }

    // Phương thức chung để ghi nhận bất kỳ sự kiện nào
    fun logCustomEvent(eventName: String, parameters: Bundle) {
        logEvent(AnalyticsEvent(eventName, parameters))
    }
}

