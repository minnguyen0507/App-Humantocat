package com.pettranslator.cattranslator.catsounds.ui.intro

import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.pettranslator.cattranslator.catsounds.BuildConfig
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.BaseActivity
import com.pettranslator.cattranslator.catsounds.databinding.ActivityIntroBinding
import com.pettranslator.cattranslator.catsounds.ui.main.MainActivity
import com.pettranslator.cattranslator.catsounds.utils.AnalyticsHelper
import com.pettranslator.cattranslator.catsounds.utils.DataProvider
import com.pettranslator.cattranslator.catsounds.utils.ScreenName
import com.pettranslator.cattranslator.catsounds.utils.SharedPref
import com.pettranslator.cattranslator.catsounds.utils.ad.AdManager
import com.pettranslator.cattranslator.catsounds.utils.isInternetConnected
import com.pettranslator.cattranslator.catsounds.utils.openActivityAndClearApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>() {

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var dataProvider: DataProvider

    @Inject
    lateinit var adManager: AdManager

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper
    private var timer: CountDownTimer? = null

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityIntroBinding =
        ActivityIntroBinding.inflate(inflater)

    override fun initialize() {
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adManager.loadNativeIntroAd(viewBinding.adContainer, onAdLoaded = {
            analyticsHelper.logShowNative(ScreenName.INTRO)

        }, onAdFailed = {
            analyticsHelper.logShowNativeFailed(ScreenName.INTRO)
            resetShowBtnNext()
        }, onAdImpression = {
            analyticsHelper.logAdImpression("native", BuildConfig.NATIVE_AD_UNIT_ID)
            resetShowBtnNext()
        })

        viewBinding.apply {
            introViewPager.adapter = IntroAdapter(dataProvider.getSlideList())
            tabIndicator.attachTo(viewBinding.introViewPager)
            introViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    when (position) {
                        0 -> {
                            analyticsHelper.logScreenView(ScreenName.ONBOARDING_1)
                            analyticsHelper.logTutorialBegin(
                                System.currentTimeMillis().toString(),
                                false
                            )

                            val prNatAd = adContainer.layoutParams as ConstraintLayout.LayoutParams
                            btnNext1.visibility = View.GONE
                            prNatAd.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                            prNatAd.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                            prNatAd.topToBottom = tabIndicator.id
                            prNatAd.bottomToBottom = ConstraintLayout.LayoutParams.UNSET

                        }

                        1 -> {
                            analyticsHelper.logScreenView(ScreenName.ONBOARDING_2)
                            btnNext1.visibility = View.GONE
                            val prNatAd = adContainer.layoutParams as ConstraintLayout.LayoutParams
                            prNatAd.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                            prNatAd.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                            prNatAd.topToBottom = tabIndicator.id
                            prNatAd.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
                        }

                        2 -> {
                            analyticsHelper.logScreenView(ScreenName.ONBOARDING_3)

                            val prNatAd = adContainer.layoutParams as ConstraintLayout.LayoutParams
                            prNatAd.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                            prNatAd.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                            prNatAd.topToBottom = ConstraintLayout.LayoutParams.UNSET
                            prNatAd.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                        }

                    }
                    resetShowBtnNext()
                }
            })
            btnNext.setOnClickListener {
                val next = viewBinding.introViewPager.currentItem + 1
                if (next < dataProvider.getSlideList().size) {
                    viewBinding.introViewPager.currentItem = next
                } else {
                    sharedPref.setFirstRun(false)
                    analyticsHelper.logTutorialComplete(System.currentTimeMillis().toString(), true)

                    lifecycleScope.launch {
                        val isOnline = isInternetConnected(this@IntroActivity)
                        if (!isOnline) {
                            openActivityAndClearApp(MainActivity::class.java)
                            return@launch
                        } else {
                            adManager.showInterstitialAdIfEligible(
                                this@IntroActivity,
                                minIntervalMillis = 25_000L,
                                adTag = "Intro",
                                onAdClosed = {
                                    dismissAdLoadingDialog()
                                    openActivityAndClearApp(MainActivity::class.java)
                                },
                                onAdSkipped = {
                                    dismissAdLoadingDialog()
                                    openActivityAndClearApp(MainActivity::class.java)
                                },
                                onAdFailedToShow = {
                                    analyticsHelper.logShowInterstitialFailed(ScreenName.SONG)
                                    dismissAdLoadingDialog()
                                    openActivityAndClearApp(MainActivity::class.java)
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
            }
            btnNext1.setOnClickListener {
                val next = viewBinding.introViewPager.currentItem + 1
                if (next < dataProvider.getSlideList().size) {
                    viewBinding.introViewPager.currentItem = next
                } else {
                    sharedPref.setFirstRun(false)
                    analyticsHelper.logTutorialComplete(System.currentTimeMillis().toString(), true)

                    lifecycleScope.launch {
                        val isOnline = isInternetConnected(this@IntroActivity)
                        if (!isOnline) {
                            openActivityAndClearApp(MainActivity::class.java)
                            return@launch
                        } else {
                            showAdLoadingDialog()
                            adManager.showInterstitialAdIfEligible(
                                this@IntroActivity,
                                minIntervalMillis = 25_000L,
                                adTag = "Intro",
                                onAdClosed = {
                                    dismissAdLoadingDialog()
                                    openActivityAndClearApp(MainActivity::class.java)
                                },
                                onAdSkipped = {
                                    dismissAdLoadingDialog()
                                    openActivityAndClearApp(MainActivity::class.java)
                                },
                                onAdFailedToShow = {
                                    analyticsHelper.logShowInterstitialFailed(ScreenName.SONG)
                                    dismissAdLoadingDialog()
                                    openActivityAndClearApp(MainActivity::class.java)
                                },
                                onAdStartShowing = {
//                                    showAdLoadingDialog()
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
            }
        }
    }

    fun resetShowBtnNext() {
        viewBinding.btnNext.visibility = View.GONE;
        timer?.cancel()
        timer = object : CountDownTimer(1500L, 1500L) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                if (viewBinding.introViewPager.currentItem < 2)
                    viewBinding.btnNext.visibility = View.VISIBLE;
                else {
                    viewBinding.btnNext1.visibility = View.VISIBLE;
                }
            }
        }.start()
    }

    override fun onResume() {
        super.onResume()
        analyticsHelper.logScreenView(ScreenName.INTRO)
    }
}