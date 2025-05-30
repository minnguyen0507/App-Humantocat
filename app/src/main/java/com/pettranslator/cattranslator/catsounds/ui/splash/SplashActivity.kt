package com.pettranslator.cattranslator.catsounds.ui.splash

import android.animation.ValueAnimator
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.core.animation.doOnEnd
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.BaseActivity
import com.pettranslator.cattranslator.catsounds.bases.RemoteConfigRepository
import com.pettranslator.cattranslator.catsounds.databinding.ActivitySplashBinding
import com.pettranslator.cattranslator.catsounds.ui.intro.IntroActivity
import com.pettranslator.cattranslator.catsounds.ui.language.LanguageActivity
import com.pettranslator.cattranslator.catsounds.ui.main.MainActivity
import com.pettranslator.cattranslator.catsounds.utils.ALog
import com.pettranslator.cattranslator.catsounds.utils.AnalyticsHelper
import com.pettranslator.cattranslator.catsounds.utils.ScreenName
import com.pettranslator.cattranslator.catsounds.utils.SharedPref
import com.pettranslator.cattranslator.catsounds.utils.ad.AdManager
import com.pettranslator.cattranslator.catsounds.utils.openActivityAndClearApp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var adManager: AdManager

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper



    override fun inflateViewBinding(inflater: LayoutInflater): ActivitySplashBinding =
        ActivitySplashBinding.inflate(inflater)

    override fun initialize() {
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        animateProgressBar()

    }

    private fun animateProgressBar() {

        val animator = ValueAnimator.ofInt(0, 100)
        animator.duration = 4000
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            viewBinding.progressBar.progress = value
            viewBinding.tvProgress.text = getString(R.string.loading, value)
        }
        animator.doOnEnd {
            checkFirstRun()
        }
        animator.start()

    }

    private fun checkFirstRun() {

        if (sharedPref.getFirstRun()) {
            openFirstRunActivity()
            return
        }
        openActivityAndClearApp(MainActivity::class.java)
    }


    private fun openFirstRunActivity() {
        if (sharedPref.getFirstLanguage()) {
            openActivityAndClearApp(LanguageActivity::class.java)
            return
        }
        openActivityAndClearApp(IntroActivity::class.java)
    }

    override fun onResume() {
        super.onResume()
        intent.getStringExtra("notification_type")?.let { type ->
            analyticsHelper.logNotificationOpen(type)
        }
        analyticsHelper.logScreenView(ScreenName.SPLASH)
    }

}