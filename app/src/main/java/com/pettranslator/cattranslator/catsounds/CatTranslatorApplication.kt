package com.pettranslator.cattranslator.catsounds

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.lifecycleScope
import androidx.work.Configuration
import com.google.android.gms.ads.MobileAds
import com.pettranslator.cattranslator.catsounds.bases.AppContainer
import com.pettranslator.cattranslator.catsounds.ui.main.AdLoadingDialogFragment
import com.pettranslator.cattranslator.catsounds.utils.ALog
import com.pettranslator.cattranslator.catsounds.utils.AnalyticsHelper
import com.pettranslator.cattranslator.catsounds.utils.SharedPref
import com.pettranslator.cattranslator.catsounds.utils.ad.AppOpenAdManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class CatTranslatorApplication : Application(),
    Application.ActivityLifecycleCallbacks, Configuration.Provider {

    private var activityReferences = 0
    private var isActivityChangingConfigurations = false
    private var isReturningFromBackground = false
    private var hasAppStartedOnce = false
    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    @Inject
    lateinit var appContainer: AppContainer

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {}
        val languageCode = sharedPref.getLanguage()
        val locales = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(locales)
        registerActivityLifecycleCallbacks(this)
        appScope.launch {
            appContainer.initializeAdConfig { success ->
                if (success) {
                    ALog.d("AppContainer", "AdConfig initialized with server data")
                } else {
                    ALog.d(
                        "AppContainer",
                        "AdConfig initialized with defaults due to fetch failure"
                    )
                }
            }
        }

    }


    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?
    ) {

    }

    private var lastAdShownTime: Long = 0
    override fun onActivityStarted(activity: Activity) {
        val wasInBackground = activityReferences == 0

        activityReferences++

        if (wasInBackground && !isActivityChangingConfigurations && hasAppStartedOnce) {
            val currentTime = System.currentTimeMillis()
            val minInterval = 30 * 1000

            if (currentTime - lastAdShownTime > minInterval) {
                isReturningFromBackground = true
                lastAdShownTime = currentTime
            }
        }

        if (!hasAppStartedOnce) {
            hasAppStartedOnce = true
        }

        if (isReturningFromBackground) {
            isReturningFromBackground = false
            AdLoadingDialogFragment.show(
                (activity as AppCompatActivity).supportFragmentManager
            )
            AppOpenAdManager.loadAd(
                context = this,
                onAdLoaded = {
                    AdLoadingDialogFragment.dismiss(
                        activity.supportFragmentManager
                    )
                    AppOpenAdManager.showAdIfAvailable(activity, onAdImpression = {
                        analyticsHelper.logAdImpression("OOA", BuildConfig.APP_OPEN_AD_UNIT_ID)
                    }, onAdClosed = {
                        AdLoadingDialogFragment.dismiss(
                            activity.supportFragmentManager
                        )
                    })
                },
                onAdFailed = {
                    AdLoadingDialogFragment.dismiss(
                        activity.supportFragmentManager
                    )
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


