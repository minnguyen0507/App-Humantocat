package com.pettranslator.cattranslator.catsounds.ui.translate

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.fragment.BaseFragment
import com.pettranslator.cattranslator.catsounds.databinding.FragmentTranslateBinding
import com.pettranslator.cattranslator.catsounds.model.ETypeTranslator
import com.pettranslator.cattranslator.catsounds.ui.record.RecordActivity
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

    override fun inflateViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentTranslateBinding = FragmentTranslateBinding.inflate(inflater)

    override fun initialize() {
        adManager.loadNativeClickAd(viewBinding.adContainer,onAdLoaded = {}, onAdFailed = {})
        viewBinding.apply {
            constraintHuman.setSafeOnClickListener {
                if (!requireActivity().isInternetConnected()) {
                    requireActivity().showToast(getString(R.string.connect_internet))
                    return@setSafeOnClickListener
                }
                adManager.showInterstitialAd(requireActivity(), onAdClosed = {
                    typeTrans = ETypeTranslator.HUMANTOANIMAL
                    requireContext().openActivity(RecordActivity::class.java)
                }, onAdFailed = { errorMessage ->{
                    requireActivity().showToast(getString(R.string.connect_internet))
                } })

            }
            constraintAnimal.setSafeOnClickListener {
                if (!requireActivity().isInternetConnected()) {
                    requireActivity().showToast(getString(R.string.connect_internet))
                    return@setSafeOnClickListener
                }
                adManager.showInterstitialAd(requireActivity(), onAdClosed = {
                    typeTrans = ETypeTranslator.CATTOHUMAN
                    requireContext().openActivity(RecordActivity::class.java)
                }, onAdFailed = { errorMessage ->{
                    requireActivity().showToast(getString(R.string.connect_internet))
                } })

            }
        }
    }

    companion object{
        var typeTrans = ETypeTranslator.HUMANTOANIMAL
    }

}