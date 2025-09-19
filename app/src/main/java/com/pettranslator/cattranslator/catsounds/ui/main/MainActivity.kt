package com.pettranslator.cattranslator.catsounds.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.pettranslator.cattranslator.catsounds.BuildConfig
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.AppContainer
import com.pettranslator.cattranslator.catsounds.bases.BaseActivity
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
import com.pettranslator.cattranslator.catsounds.utils.isInternetConnected
import com.pettranslator.cattranslator.catsounds.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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

    private lateinit var navItems: List<Pair<LinearLayout, Pair<ImageView, TextView>>>
    private var isFirstShow: Boolean = true;
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
        } ?: run {}
    }

    private fun initCustomNav() {
        val customNavBar = findViewById<View>(R.id.customNavBar)

        navItems = listOf(
            R.id.nav_home, R.id.nav_translate, R.id.nav_game, R.id.nav_music, R.id.nav_setting
        ).map { id ->
            val layout = customNavBar.findViewById<LinearLayout>(id)
            val image = layout.getChildAt(0) as ImageView
            val text = layout.getChildAt(1) as TextView
            layout.setOnClickListener { onNavItemSelected(id) }
            Pair(layout, Pair(image, text))
        }

        onNavItemSelected(R.id.nav_home)
    }

    private fun onNavItemSelected(selectedId: Int) {
        navItems.forEach { (layout, viewPair) ->
            val (imageView, textView) = viewPair
            val isSelected = layout.id == selectedId

            val tintColor = if (isSelected) {
                ContextCompat.getColor(this, R.color.primaryColor)
            } else {
                ContextCompat.getColor(this, R.color.app_gray)
            }
            imageView.setColorFilter(tintColor, android.graphics.PorterDuff.Mode.SRC_IN)
            textView.setTextColor(tintColor)
        }
        if (appContainer.adConfig?.interEnabledHome == true) {
            when (selectedId) {
                R.id.nav_home -> {
                    if (!isFirstShow) lifecycleScope.launch {
                        val isOnline = isInternetConnected(this@MainActivity)
                        if (!isOnline) {
                            viewBinding.viewPager.setCurrentItem(0, true)
                            return@launch
                        } else {
                            viewBinding.viewPager.setCurrentItem(0, true)
                            ALog.d(
                                "themd",
                                "interDelayHome: ${appContainer.adConfig?.interDelayHomeSec}"
                            )
                            adManager.showInterstitialAdIfEligible(
                                this@MainActivity,
                                minIntervalMillis = 25_000L,
                                adTag = "Home",
                                onAdClosed = {
                                    dismissAdLoadingDialog()
                                },
                                onAdSkipped = {
                                    dismissAdLoadingDialog()
                                },
                                onAdFailedToShow = {
                                    analyticsHelper.logShowInterstitialFailed(ScreenName.HOME)
                                    dismissAdLoadingDialog()
                                },
                                onAdStartShowing = {
                                    ALog.d("themd", "onAdStartShowing")
                                    showAdLoadingDialog()
                                },
                                onAdImpression = {
                                    analyticsHelper.logShowInterstitial(ScreenName.HOME)
                                    analyticsHelper.logAdImpression(
                                        "interstitial", BuildConfig.INTERSTITIAL_AD_UNIT_ID
                                    )
                                })
                        }
                    }
                }

                R.id.nav_translate -> {
                    if (!isFirstShow) lifecycleScope.launch {
                        val isOnline = isInternetConnected(this@MainActivity)
                        if (!isOnline) {
                            viewBinding.viewPager.setCurrentItem(1, true)
                            return@launch
                        } else {
                            viewBinding.viewPager.setCurrentItem(1, true)
                            adManager.showInterstitialAdIfEligible(
                                this@MainActivity,
                                minIntervalMillis = 25_000L,
                                adTag = "Translate",
                                onAdClosed = {
                                    dismissAdLoadingDialog()

                                },
                                onAdSkipped = {
                                    dismissAdLoadingDialog()
                                },
                                onAdFailedToShow = {
                                    analyticsHelper.logShowInterstitialFailed(ScreenName.TRANSLATE)
                                    dismissAdLoadingDialog()
                                },
                                onAdStartShowing = {
                                    ALog.d("themd", "onAdStartShowing")
                                    showAdLoadingDialog()
                                },
                                onAdImpression = {
                                    analyticsHelper.logShowInterstitial(ScreenName.TRANSLATE)
                                    analyticsHelper.logAdImpression(
                                        "interstitial", BuildConfig.INTERSTITIAL_AD_UNIT_ID
                                    )
                                })
                        }
                    }
                }

                R.id.nav_game -> {
                    if (!isFirstShow) lifecycleScope.launch {
                        val isOnline = isInternetConnected(this@MainActivity)
                        if (!isOnline) {
                            viewBinding.viewPager.setCurrentItem(2, true)
                            return@launch
                        } else {
                            viewBinding.viewPager.setCurrentItem(2, true)
                            adManager.showInterstitialAdIfEligible(
                                this@MainActivity,
                                adTag = "Game",
                                minIntervalMillis = 25_000L,
                                onAdClosed = {
                                    dismissAdLoadingDialog()
                                },
                                onAdSkipped = {
                                    dismissAdLoadingDialog()
                                },
                                onAdFailedToShow = {
                                    analyticsHelper.logShowInterstitialFailed(ScreenName.GAME)
                                    dismissAdLoadingDialog()
                                },
                                onAdStartShowing = {
                                    showAdLoadingDialog()
                                },
                                onAdImpression = {
                                    analyticsHelper.logShowInterstitial(ScreenName.GAME)
                                    analyticsHelper.logAdImpression(
                                        "interstitial", BuildConfig.INTERSTITIAL_AD_UNIT_ID
                                    )
                                })
                        }
                    }
                }

                R.id.nav_music -> {
                    if (!isFirstShow) lifecycleScope.launch {
                        val isOnline = isInternetConnected(this@MainActivity)
                        if (!isOnline) {
                            viewBinding.viewPager.setCurrentItem(3, true)
                            return@launch
                        } else {
                            viewBinding.viewPager.setCurrentItem(3, true)
                            adManager.showInterstitialAdIfEligible(
                                this@MainActivity,
                                minIntervalMillis = 25_000L,
                                adTag = "Music",
                                onAdClosed = {
                                    dismissAdLoadingDialog()
                                },
                                onAdSkipped = {
                                    dismissAdLoadingDialog()
                                },
                                onAdFailedToShow = {
                                    analyticsHelper.logShowInterstitialFailed(ScreenName.SONG)
                                    dismissAdLoadingDialog()
                                },
                                onAdStartShowing = {
                                    showAdLoadingDialog()
                                },
                                onAdImpression = {
                                    analyticsHelper.logShowInterstitial(ScreenName.SONG)
                                    analyticsHelper.logAdImpression(
                                        "interstitial", BuildConfig.INTERSTITIAL_AD_UNIT_ID
                                    )
                                })
                        }
                    }
                }

                R.id.nav_setting -> {
                    viewBinding.viewPager.setCurrentItem(4, true)
                }
            }
        } else {
            when (selectedId) {
                R.id.nav_home -> {
                    lifecycleScope.launch {
                        val isOnline = isInternetConnected(this@MainActivity)
                        if (!isOnline) {
                            viewBinding.viewPager.setCurrentItem(0, true)
                            return@launch
                        } else {
                            viewBinding.viewPager.setCurrentItem(0, true)
                            ALog.d(
                                "themd",
                                "interDelayHome: ${appContainer.adConfig?.interDelayHomeSec}"
                            )
                            adManager.showInterstitialAdIfEligible(
                                this@MainActivity,
                                minIntervalMillis = appContainer.adConfig?.interDelayHomeSec?.times(
                                    1000L
                                ) ?: 60_000L,
                                adTag = "Home",
                                onAdClosed = {
                                    dismissAdLoadingDialog()

                                },
                                onAdSkipped = {
                                    dismissAdLoadingDialog()
                                },
                                onAdFailedToShow = {
                                    analyticsHelper.logShowInterstitialFailed(ScreenName.HOME)
                                    dismissAdLoadingDialog()
                                },
                                onAdStartShowing = {
                                    ALog.d("themd", "onAdStartShowing")
                                },
                                onAdImpression = {
                                    analyticsHelper.logShowInterstitial(ScreenName.HOME)
                                    analyticsHelper.logAdImpression(
                                        "interstitial", BuildConfig.INTERSTITIAL_AD_UNIT_ID
                                    )
                                })
                        }
                    }
                }


                R.id.nav_translate -> {
                    lifecycleScope.launch {
                        val isOnline = isInternetConnected(this@MainActivity)
                        if (!isOnline) {
                            viewBinding.viewPager.setCurrentItem(1, true)
                            return@launch
                        } else {
                            viewBinding.viewPager.setCurrentItem(1, true)
                            adManager.showInterstitialAdIfEligible(
                                this@MainActivity,
                                minIntervalMillis = appContainer.adConfig?.interDelayTranslateSec?.times(
                                    1000L
                                ) ?: 30_000L,
                                adTag = "Translate",
                                onAdClosed = {
                                    dismissAdLoadingDialog()

                                },
                                onAdSkipped = {
                                    dismissAdLoadingDialog()
                                },
                                onAdFailedToShow = {
                                    analyticsHelper.logShowInterstitialFailed(ScreenName.TRANSLATE)
                                    dismissAdLoadingDialog()
                                },
                                onAdStartShowing = {
                                    ALog.d("themd", "onAdStartShowing")
                                    showAdLoadingDialog()
                                },
                                onAdImpression = {
                                    analyticsHelper.logShowInterstitial(ScreenName.TRANSLATE)
                                    analyticsHelper.logAdImpression(
                                        "interstitial", BuildConfig.INTERSTITIAL_AD_UNIT_ID
                                    )
                                })
                        }
                    }
                }

                R.id.nav_game -> {
                    lifecycleScope.launch {
                        val isOnline = isInternetConnected(this@MainActivity)
                        if (!isOnline) {
                            viewBinding.viewPager.setCurrentItem(2, true)
                            return@launch
                        } else {
                            viewBinding.viewPager.setCurrentItem(2, true)
                            adManager.showInterstitialAdIfEligible(
                                this@MainActivity,
                                adTag = "Game",
                                minIntervalMillis = appContainer.adConfig?.interDelayGameSec?.times(
                                    1000L
                                ) ?: 30_000L,
                                onAdClosed = {
                                    dismissAdLoadingDialog()
                                },
                                onAdSkipped = {
                                    dismissAdLoadingDialog()
                                },
                                onAdFailedToShow = {
                                    analyticsHelper.logShowInterstitialFailed(ScreenName.GAME)
                                    dismissAdLoadingDialog()
                                },
                                onAdStartShowing = {
                                    showAdLoadingDialog()
                                },
                                onAdImpression = {
                                    analyticsHelper.logShowInterstitial(ScreenName.GAME)
                                    analyticsHelper.logAdImpression(
                                        "interstitial", BuildConfig.INTERSTITIAL_AD_UNIT_ID
                                    )
                                })
                        }
                    }
                }

                R.id.nav_music -> {
                    lifecycleScope.launch {
                        val isOnline = isInternetConnected(this@MainActivity)
                        if (!isOnline) {
                            viewBinding.viewPager.setCurrentItem(3, true)
                            return@launch
                        } else {
                            viewBinding.viewPager.setCurrentItem(3, true)
                            adManager.showInterstitialAdIfEligible(
                                this@MainActivity,
                                minIntervalMillis = appContainer.adConfig?.interDelaySongsSec?.times(
                                    1000L
                                ) ?: 30_000L,
                                adTag = "Song",
                                onAdClosed = {
                                    dismissAdLoadingDialog()
                                },
                                onAdSkipped = {
                                    dismissAdLoadingDialog()
                                },
                                onAdFailedToShow = {
                                    analyticsHelper.logShowInterstitialFailed(ScreenName.SONG)
                                    dismissAdLoadingDialog()
                                },
                                onAdStartShowing = {
                                    showAdLoadingDialog()
                                },
                                onAdImpression = {
                                    analyticsHelper.logShowInterstitial(ScreenName.SONG)
                                    analyticsHelper.logAdImpression(
                                        "interstitial", BuildConfig.INTERSTITIAL_AD_UNIT_ID
                                    )
                                })
                        }
                    }
                }

                R.id.nav_setting -> viewBinding.viewPager.setCurrentItem(4, true)
            }
        }
        isFirstShow = false;
    }

    private fun checkPermissionNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
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
        initCustomNav()
    }

    fun loadBannerAd() {
        if (appContainer.adConfig?.bannerEnabled == false) return
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
        adManager.loadRewardAdIfNeeded(activity = this);
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
        viewBinding.dialogConfirm.frame.setOnClickListener {
            viewBinding.dialogConfirm.root.visibility = View.GONE
        }
    }
}