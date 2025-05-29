package com.pettranslator.cattranslator.catsounds

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.android.gms.ads.MobileAds
import com.pettranslator.cattranslator.catsounds.utils.SharedPref
import com.pettranslator.cattranslator.catsounds.utils.ad.AppOpenAdManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class CatTranslatorApplication : Application(),
    Application.ActivityLifecycleCallbacks , Configuration.Provider{

    private var activityReferences = 0
    private var isActivityChangingConfigurations = false
    private var isReturningFromBackground = false
    private var hasAppStartedOnce = false
    @Inject lateinit var sharedPref: SharedPref

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()


    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {}
        val languageCode = sharedPref.getLanguage()
        val locales = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(locales)
        registerActivityLifecycleCallbacks(this)
    }


    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?
    ) {

    }

    override fun onActivityStarted(activity: Activity) {
        val wasInBackground = activityReferences == 0

        activityReferences++

        if (wasInBackground && !isActivityChangingConfigurations && hasAppStartedOnce) {
            isReturningFromBackground = true
        }

        if (!hasAppStartedOnce) {
            hasAppStartedOnce = true
        }

        if (isReturningFromBackground) {
            isReturningFromBackground = false
            AppOpenAdManager.loadAd(
                context = this,
                onAdLoaded = {
                    AppOpenAdManager.showAdIfAvailable(activity) {
                        // Ad closed
                    }
                },
                onAdFailed = {
                    // Failed to load
                }
            )
        }
    }

    override fun onActivityStopped(activity: Activity) {
        isActivityChangingConfigurations = activity.isChangingConfigurations
        activityReferences--
    }

    override fun onActivityResumed(activity: Activity) {

    }


    override fun onActivityPaused(activity: Activity) {
        //TODO("Not yet implemented")
    }

    override fun onActivitySaveInstanceState(
        activity: Activity,
        outState: Bundle
    ) {
        //TODO("Not yet implemented")
    }

    override fun onActivityDestroyed(activity: Activity) {
        //TODO("Not yet implemented")
    }



}


