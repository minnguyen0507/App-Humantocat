package com.pettranslator.cattranslator.catsounds.ui.main

import android.view.LayoutInflater
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.BaseActivity
import com.pettranslator.cattranslator.catsounds.bases.ViewPagerAdapter
import com.pettranslator.cattranslator.catsounds.databinding.ActivityMainBinding
import com.pettranslator.cattranslator.catsounds.ui.game.GameFragment
import com.pettranslator.cattranslator.catsounds.ui.home.HomeFragment
import com.pettranslator.cattranslator.catsounds.ui.music.MusicFragment
import com.pettranslator.cattranslator.catsounds.ui.setting.SettingFragment
import com.pettranslator.cattranslator.catsounds.ui.translate.TranslateFragment
import com.pettranslator.cattranslator.catsounds.utils.ad.AdManager
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var viewPage: ViewPagerAdapter
    @Inject
    lateinit var adManager: AdManager

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityMainBinding =
        ActivityMainBinding.inflate(inflater)

    override fun initialize() {
        viewPage = ViewPagerAdapter(supportFragmentManager, lifecycle)
        viewPage.add(0) { HomeFragment() }
        viewPage.add(1) { TranslateFragment() }
        viewPage.add(2) { GameFragment() }
        viewPage.add(3) { MusicFragment() }
        viewPage.add(4) { SettingFragment() }
        viewBinding.viewPager.adapter = viewPage
        viewBinding.viewPager.offscreenPageLimit = 1
        viewBinding.viewPager.isUserInputEnabled = false
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

        loadBannerAd()
    }

    fun loadBannerAd() {
        adManager.loadBannerAd(viewBinding.adView)
    }

}