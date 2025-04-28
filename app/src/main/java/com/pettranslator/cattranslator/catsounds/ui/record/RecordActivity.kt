package com.pettranslator.cattranslator.catsounds.ui.record

import android.Manifest
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.BaseActivity
import com.pettranslator.cattranslator.catsounds.databinding.ActivityRecordBinding
import com.pettranslator.cattranslator.catsounds.model.ETypeTranslator
import com.pettranslator.cattranslator.catsounds.ui.translate.TranslateFragment
import com.pettranslator.cattranslator.catsounds.utils.AnalyticsHelper
import com.pettranslator.cattranslator.catsounds.utils.DataProvider
import com.pettranslator.cattranslator.catsounds.utils.ScreenName
import com.pettranslator.cattranslator.catsounds.utils.ad.AdManager
import com.pettranslator.cattranslator.catsounds.utils.openActivity
import com.pettranslator.cattranslator.catsounds.utils.setSafeOnClickListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecordActivity : BaseActivity<ActivityRecordBinding>() {

    @Inject
    lateinit var adManager: AdManager

    private var seconds = 0

    @Inject
    lateinit var dataProvider: DataProvider

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private var isRunning = false
    private val RECORD_AUDIO_REQUEST_CODE = 1002
    private val timerRunnable = object : Runnable {
        override fun run() {
            val minutes = seconds / 60
            val sec = seconds % 60
            viewBinding.tvTime.text = String.format("%02d:%02d", minutes, sec)
            seconds++
            handler.postDelayed(this, 1000)
        }
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityRecordBinding =
        ActivityRecordBinding.inflate(inflater)

    override fun initialize() {
        enableEdgeToEdge()

        analyticsHelper.logScreenView(ScreenName.RECORD)

        adManager.loadNativeClickAd(viewBinding.adContainer, onAdLoaded = {
            analyticsHelper.logShowNative(ScreenName.RECORD)
        }, onAdFailed = {
            analyticsHelper.logShowNativeFailed(ScreenName.RECORD)
        })
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewBinding.btnBack.setSafeOnClickListener {
            finish()
        }

        viewBinding.buttonRecord.setSafeOnClickListener {
            requestPermission(Manifest.permission.RECORD_AUDIO, RECORD_AUDIO_REQUEST_CODE)
        }
    }

    private fun startRecord() {
        if (!isRunning) {
            seconds = 0
            isRunning = true
            handler.post(timerRunnable)
            zoomInOutObject(viewBinding.imageView9)
            return
        }
        isRunning = false
        zoomInOutObject(viewBinding.imageView9)
        handler.removeCallbacks(timerRunnable)
        if (TranslateFragment.typeTrans == ETypeTranslator.HUMANTOANIMAL) {
            openActivity(ResultRecordActivity::class.java) {
                putInt(SECONDS, seconds - 1)
            }
        } else {
            val listCatResult = dataProvider.getCatSounds()
            val catResult = listCatResult.random()
            val dialog = TranslateResultDialog(this, catResult) {

            }
            dialog.show()
        }
    }

    private fun zoomInOutObject(view: View) {
        val scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.2f)
        val scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.2f)
        val scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1.2f, 1f)
        val scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1.2f, 1f)

        val animatorSet = AnimatorSet()
        animatorSet.play(scaleUpX).with(scaleUpY)
        animatorSet.play(scaleDownX).with(scaleDownY).after(scaleUpX)
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                super.onAnimationEnd(animation)
                if (isRunning) {
                    animatorSet.start()
                }
            }
        })
        animatorSet.duration = 350

        if (isRunning) animatorSet.start() else animatorSet.cancel()
    }


    override fun performPermissionTask() {
        super.performPermissionTask()
        startRecord()
    }

    companion object {
        const val SECONDS = "seconds"
    }
}