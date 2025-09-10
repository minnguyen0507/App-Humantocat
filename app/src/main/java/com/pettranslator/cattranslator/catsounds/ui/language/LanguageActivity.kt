package com.pettranslator.cattranslator.catsounds.ui.language

import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pettranslator.cattranslator.catsounds.BuildConfig
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.BaseActivity
import com.pettranslator.cattranslator.catsounds.databinding.ActivityLanguageBinding
import com.pettranslator.cattranslator.catsounds.model.LanguageItem
import com.pettranslator.cattranslator.catsounds.ui.intro.IntroActivity
import com.pettranslator.cattranslator.catsounds.utils.AnalyticsHelper
import com.pettranslator.cattranslator.catsounds.utils.DataProvider
import com.pettranslator.cattranslator.catsounds.utils.ScreenName
import com.pettranslator.cattranslator.catsounds.utils.SharedPref
import com.pettranslator.cattranslator.catsounds.utils.ad.AdManager
import com.pettranslator.cattranslator.catsounds.utils.openActivityAndClearApp
import com.pettranslator.cattranslator.catsounds.utils.setSafeOnClickListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LanguageActivity : BaseActivity<ActivityLanguageBinding>() {

    private var langCode = "en"

    @Inject
    lateinit var adManager: AdManager

    @Inject
    lateinit var dataProvider: DataProvider

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private var listLanguage = mutableListOf<LanguageItem>()

    private lateinit var languageAdapter: LanguageAdapter
    private var timer: CountDownTimer? = null

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityLanguageBinding =
        ActivityLanguageBinding.inflate(inflater)

    override fun initialize() {
        enableEdgeToEdge()
        viewBinding.btnApply.visibility = View.GONE;
        listLanguage = getLanguages()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        adManager.loadNativeIntroAd(viewBinding.adContainer, onAdLoaded = {
            analyticsHelper.logShowNative(ScreenName.LANGUAGE)
        }, onAdFailed = {
            analyticsHelper.logShowNativeFailed(ScreenName.LANGUAGE)
        }, onAdImpression = {
            analyticsHelper.logAdImpression("native", BuildConfig.NATIVE_AD_UNIT_ID)
        })
        languageAdapter = LanguageAdapter()
        viewBinding.apply {
            rcvLanguage.adapter = languageAdapter
            btnBack.setSafeOnClickListener {
                setResult(RESULT_CANCELED)
                finish()
            }
            btnApply.setSafeOnClickListener {
                val locales = LocaleListCompat.forLanguageTags(langCode)
                AppCompatDelegate.setApplicationLocales(locales)
                sharedPref.saveLanguage(langCode)
                analyticsHelper.logLanguageSelect(langCode)
                if (sharedPref.getFirstLanguage()) {
                    openActivityAndClearApp(IntroActivity::class.java)
                    sharedPref.setFirstLanguage(false)
                    return@setSafeOnClickListener
                }
                setResult(RESULT_OK)
                finish()
            }
        }

        languageAdapter.registerItemClickListener { view, language, postion ->
            listLanguage.forEach { it.isSelected = false }
            language.isSelected = true
//            if (viewBinding.btnApply.isGone)
            delayShowTick()
            langCode = language.localeCode
        }

        languageAdapter.addData(listLanguage)
    }

    private fun delayShowTick() {
        timer?.cancel()
        viewBinding.btnApply.visibility = View.GONE;
        timer = object : CountDownTimer(1500L, 1500L) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                println("ON FINISH")
                viewBinding.btnApply.visibility = View.VISIBLE;
            }
        }.start()
    }

    override fun onResume() {
        super.onResume()
        analyticsHelper.logScreenView(ScreenName.LANGUAGE)
    }

    private fun getLanguages(): MutableList<LanguageItem> {
        return dataProvider.getLanguageList().toMutableList()
    }
}