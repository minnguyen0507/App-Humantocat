package com.pettranslator.cattranslator.catsounds.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.pettranslator.cattranslator.catsounds.BuildConfig
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.AppContainer
import com.pettranslator.cattranslator.catsounds.bases.BaseActivity
import com.pettranslator.cattranslator.catsounds.bases.RemoteConfigRepository
import com.pettranslator.cattranslator.catsounds.bases.ViewPagerAdapter
import com.pettranslator.cattranslator.catsounds.databinding.ActivityMainBinding
import com.pettranslator.cattranslator.catsounds.ui.game.GameFragment
import com.pettranslator.cattranslator.catsounds.ui.home.HomeFragment
import com.pettranslator.cattranslator.catsounds.ui.music.SongFragment
import com.pettranslator.cattranslator.catsounds.ui.setting.SettingFragment
import com.pettranslator.cattranslator.catsounds.ui.translate.TranslateFragment
import com.pettranslator.cattranslator.catsounds.utils.ALog
import com.pettranslator.cattranslator.catsounds.utils.AnalyticsHelper
import com.pettranslator.cattranslator.catsounds.utils.NotificationScheduler
import com.pettranslator.cattranslator.catsounds.utils.ScreenName
import com.pettranslator.cattranslator.catsounds.utils.SharedPref
import com.pettranslator.cattranslator.catsounds.utils.ad.AdManager
import com.pettranslator.cattranslator.catsounds.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var viewPage: ViewPagerAdapter

    @Inject
    lateinit var adManager: AdManager

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var appContainer: AppContainer

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            NotificationScheduler.scheduleBoth(this)
        } else {
            showToast("")
        }
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityMainBinding =
        ActivityMainBinding.inflate(inflater)

    override fun initialize() {
        setUpViewPage()
        loadBannerAd()
        checkPermissionNotification()
        checkAndLogAppUpdate()
        appContainer.adConfig?.let { config ->
            ALog.d("AppContainer", "Interstitial Delay for Record: $config")

        } ?: run {

        }
    }

    private fun checkPermissionNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                NotificationScheduler.scheduleBoth(this)
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            NotificationScheduler.scheduleBoth(this)
        }
    }

    private fun setUpViewPage() {
        viewPage = ViewPagerAdapter(supportFragmentManager, lifecycle)
        viewPage.add(HomeFragment())
        viewPage.add(TranslateFragment())
        viewPage.add(GameFragment())
        viewPage.add(SongFragment())
        viewPage.add(SettingFragment())
        viewBinding.viewPager.apply {
            adapter = viewPage
            offscreenPageLimit = 1
            isUserInputEnabled = false
        }

        viewBinding.navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> viewBinding.viewPager.setCurrentItem(0, true)
                R.id.navigation_translate -> {

                    adManager.showInterstitialAdIfEligible(
                        this,
                        minIntervalMillis = appContainer.adConfig?.interDelayTranslateSec?.times(1000L)
                            ?: 30_000L,
                        adTag = "Translate",
                        onAdClosed = {
                            dismissAdLoadingDialog()

                        },
                        onAdSkipped = {
                            dismissAdLoadingDialog()
                        },
                        onAdFailedToShow = {
                            dismissAdLoadingDialog()
                        },
                        onAdStartShowing = {
                            ALog.d("themd", "onAdStartShowing")
                            showAdLoadingDialog()
                        }, onAdImpression = {
                            analyticsHelper.logAdImpression(
                                "interstitial",
                                BuildConfig.INTERSTITIAL_AD_UNIT_ID
                            )
                        })
                    viewBinding.viewPager.setCurrentItem(1, true)

                }
                R.id.navigation_game -> {
                    adManager.showInterstitialAdIfEligible(
                        this,
                        adTag = "Game",
                        minIntervalMillis = appContainer.adConfig?.interDelayGameSec?.times(1000L)
                            ?: 30_000L,
                        onAdClosed = {
                            dismissAdLoadingDialog()
                        },
                        onAdSkipped = {
                            dismissAdLoadingDialog()
                        },
                        onAdFailedToShow = {
                            dismissAdLoadingDialog()
                        },
                        onAdStartShowing = {
                            showAdLoadingDialog()
                        }, onAdImpression = {
                            analyticsHelper.logAdImpression(
                                "interstitial",
                                BuildConfig.INTERSTITIAL_AD_UNIT_ID
                            )
                        }
                    )
                    viewBinding.viewPager.setCurrentItem(2, true)
                }
                R.id.navigation_music -> {
                    adManager.showInterstitialAdIfEligible(
                        this,
                        minIntervalMillis = appContainer.adConfig?.interDelaySongsSec?.times(1000L)
                            ?: 30_000L,
                        adTag = "Song",
                        onAdClosed = {
                            dismissAdLoadingDialog()
                        },
                        onAdSkipped = {
                            dismissAdLoadingDialog()
                        },
                        onAdFailedToShow = {
                            dismissAdLoadingDialog()
                        },
                        onAdStartShowing = {
                            showAdLoadingDialog()
                        }, onAdImpression = {
                            analyticsHelper.logAdImpression(
                                "interstitial",
                                BuildConfig.INTERSTITIAL_AD_UNIT_ID
                            )
                        })
                    viewBinding.viewPager.setCurrentItem(3, true)
                }
                R.id.navigation_setting -> viewBinding.viewPager.setCurrentItem(4, true)
            }
            true
        }
    }

    fun loadBannerAd() {
        adManager.loadBannerAd(viewBinding.adView, onAdLoaded = {
            analyticsHelper.logShowBanner(ScreenName.MAIN)
        }, onAdFailed = {
            analyticsHelper.logShowBannerFailed(ScreenName.MAIN)
        }, onAdImpression = {
            analyticsHelper.logAdImpression("banner", BuildConfig.BANNER_AD_UNIT_ID)
        })
    }

    override fun onResume() {
        super.onResume()
        analyticsHelper.logScreenView(ScreenName.MAIN)
        adManager.loadInterstitialAdIfNeeded(activity = this)
    }

    private fun checkAndLogAppUpdate() {
        val savedVersion = sharedPref.getVersion()
        val packageInfo = this.packageManager.getPackageInfo(this.packageName, 0)
        val currentVersion = packageInfo.versionName ?: "0.0.1"
        ALog.d("themd", "savedVersion: $savedVersion, currentVersion: $currentVersion")
        if (savedVersion != currentVersion) {
            sharedPref.saveVersion(currentVersion)
            analyticsHelper.logAppUpdate(currentVersion)
        }
    }

}