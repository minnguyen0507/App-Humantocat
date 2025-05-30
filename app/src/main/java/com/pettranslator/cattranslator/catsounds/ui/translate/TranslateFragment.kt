package com.pettranslator.cattranslator.catsounds.ui.translate

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.fragment.BaseFragment
import com.pettranslator.cattranslator.catsounds.databinding.FragmentTranslateBinding
import com.pettranslator.cattranslator.catsounds.model.ETypeTranslator
import com.pettranslator.cattranslator.catsounds.ui.record.RecordActivity
import com.pettranslator.cattranslator.catsounds.utils.AnalyticsHelper
import com.pettranslator.cattranslator.catsounds.utils.ScreenName
import com.pettranslator.cattranslator.catsounds.utils.ad.AdManager
import com.pettranslator.cattranslator.catsounds.utils.isInternetConnected
import com.pettranslator.cattranslator.catsounds.utils.openActivity
import com.pettranslator.cattranslator.catsounds.utils.setSafeOnClickListener
import com.pettranslator.cattranslator.catsounds.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class TranslateFragment : BaseFragment<FragmentTranslateBinding>() {

    @Inject
    lateinit var adManager: AdManager

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    override fun inflateViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentTranslateBinding = FragmentTranslateBinding.inflate(inflater)

    override fun initialize() {

        analyticsHelper.logScreenView(ScreenName.TRANSLATE)

        adManager.loadNativeClickAd(viewBinding.adContainer, onAdLoaded = {
            analyticsHelper.logShowNative(ScreenName.TRANSLATE)
        }, onAdFailed = {
            analyticsHelper.logShowNativeFailed(ScreenName.TRANSLATE)
        })

        viewBinding.apply {
            constraintHuman.setSafeOnClickListener {
                goToRecordActivity(ETypeTranslator.HUMANTOANIMAL)
            }
            constraintAnimal.setSafeOnClickListener {
                goToRecordActivity(ETypeTranslator.CATTOHUMAN)

            }
        }
    }

    private fun goToRecordActivity(type: ETypeTranslator) {
        if (!requireActivity().isInternetConnected()) {
            requireActivity().showToast(getString(R.string.connect_internet))
            return
        }
        showAdLoadingDialog()
        adManager.showInterstitialAd(requireActivity(), onAdClosed = {
            dismissAdLoadingDialog()
            typeTrans = type
            requireContext().openActivity(RecordActivity::class.java)
            analyticsHelper.logShowInterstitial(ScreenName.TRANSLATE)
        }, onAdFailed = { errorMessage ->
            {
                dismissAdLoadingDialog()
                analyticsHelper.logShowInterstitialFailed(ScreenName.TRANSLATE)
                requireActivity().showToast(getString(R.string.connect_internet))
            }
        }, onAdLoaded = {
            dismissAdLoadingDialog()
        })
    }

    companion object {
        var typeTrans = ETypeTranslator.HUMANTOANIMAL
    }

}