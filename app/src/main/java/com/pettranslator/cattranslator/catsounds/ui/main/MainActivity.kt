package com.pettranslator.cattranslator.catsounds.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.BaseActivity
import com.pettranslator.cattranslator.catsounds.bases.ViewPagerAdapter
import com.pettranslator.cattranslator.catsounds.databinding.ActivityMainBinding
import com.pettranslator.cattranslator.catsounds.ui.game.GameFragment
import com.pettranslator.cattranslator.catsounds.ui.home.HomeFragment
import com.pettranslator.cattranslator.catsounds.ui.music.SongFragment
import com.pettranslator.cattranslator.catsounds.ui.setting.SettingFragment
import com.pettranslator.cattranslator.catsounds.ui.translate.TranslateFragment
import com.pettranslator.cattranslator.catsounds.utils.AnalyticsHelper
import com.pettranslator.cattranslator.catsounds.utils.NotificationScheduler
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
                R.id.navigation_translate -> viewBinding.viewPager.setCurrentItem(1, true)
                R.id.navigation_game -> viewBinding.viewPager.setCurrentItem(2, true)
                R.id.navigation_music -> viewBinding.viewPager.setCurrentItem(3, true)
                R.id.navigation_setting -> viewBinding.viewPager.setCurrentItem(4, true)
            }
            true
        }
    }

    fun loadBannerAd() {
        adManager.loadBannerAd(viewBinding.adView, onAdLoaded = {
            analyticsHelper.logShowBanner("Main")
        }, onAdFailed = {
            analyticsHelper.logShowBannerFailed("Main")
        })
    }

    private fun checkAndLogAppUpdate() {
        val savedVersion = sharedPref.getVersion()
        val packageInfo = this.packageManager.getPackageInfo(this.packageName, 0)
        val currentVersion = packageInfo.versionName ?: "0.0.1"
        if (savedVersion != currentVersion) {
            sharedPref.saveVersion(currentVersion)
            analyticsHelper.logAppUpdate(currentVersion)
        }
    }
}