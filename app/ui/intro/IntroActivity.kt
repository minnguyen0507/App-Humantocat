package com.pettranslator.cattranslator.catsounds.ui.intro

import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.BaseActivity
import com.pettranslator.cattranslator.catsounds.databinding.ActivityIntroBinding
import com.pettranslator.cattranslator.catsounds.model.IntroSlide
import com.pettranslator.cattranslator.catsounds.ui.main.MainActivity
import com.pettranslator.cattranslator.catsounds.utils.DataProvider
import com.pettranslator.cattranslator.catsounds.utils.SharedPref
import com.pettranslator.cattranslator.catsounds.utils.ad.AdManager
import com.pettranslator.cattranslator.catsounds.utils.openActivityAndClearApp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>() {
    @Inject
    lateinit var sharedPref: SharedPref

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityIntroBinding =
        ActivityIntroBinding.inflate(inflater)

    @Inject
    lateinit var dataProvider: DataProvider
    @Inject
    lateinit var adManager: AdManager

    override fun initialize() {
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        adManager.loadNativeClickAd(viewBinding.adContainer, onAdLoaded = {}, onAdFailed = {})
        viewBinding.introViewPager.adapter = IntroAdapter(dataProvider.getSlideList())


        viewBinding.tabIndicator.attachTo(viewBinding.introViewPager)

        // Next button
        viewBinding.btnNext.setOnClickListener {
            val next = viewBinding.introViewPager.currentItem + 1
            if (next < dataProvider.getSlideList().size) {
                viewBinding.introViewPager.currentItem = next
            } else {
                sharedPref.setFirstRun(false)
                adManager.showInterstitialAd(
                    this,
                    onAdClosed = {
                        openActivityAndClearApp(MainActivity::class.java)
                    },
                    onAdFailed = { _ ->
                        openActivityAndClearApp(MainActivity::class.java)
                    }
                )
            }
        }
    }

}