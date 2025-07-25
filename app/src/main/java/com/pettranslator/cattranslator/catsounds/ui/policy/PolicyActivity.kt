package com.pettranslator.cattranslator.catsounds.ui.policy

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.BaseActivity
import com.pettranslator.cattranslator.catsounds.databinding.ActivityPolicyBinding
import com.pettranslator.cattranslator.catsounds.utils.AnalyticsHelper
import com.pettranslator.cattranslator.catsounds.utils.ScreenName
import com.pettranslator.cattranslator.catsounds.utils.setSafeOnClickListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PolicyActivity : BaseActivity<ActivityPolicyBinding>() {

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityPolicyBinding =
        ActivityPolicyBinding.inflate(inflater)


    @SuppressLint("SetJavaScriptEnabled")
    override fun initialize() {
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewBinding.apply {
            btnBack.setSafeOnClickListener {
                finish()
            }
            val settings = webViewPolicy.settings
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            webViewPolicy.webViewClient = WebViewClient()
            webViewPolicy.loadUrl("https://sites.google.com/boomstudio.vn/privacypolicyforboomgamesjsc")
        }
    }


    override fun onResume() {
        super.onResume()
        analyticsHelper.logScreenView(ScreenName.POLICY)
    }
}