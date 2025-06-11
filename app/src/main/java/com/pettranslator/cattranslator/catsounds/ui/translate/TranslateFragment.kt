package com.pettranslator.cattranslator.catsounds.ui.translate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.pettranslator.cattranslator.catsounds.BuildConfig
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.AppContainer
import com.pettranslator.cattranslator.catsounds.bases.fragment.BaseFragment
import com.pettranslator.cattranslator.catsounds.databinding.FragmentTranslateBinding
import com.pettranslator.cattranslator.catsounds.model.ETypeTranslator
import com.pettranslator.cattranslator.catsounds.ui.music.SongFragment.Companion.CURRENT_INDEX
import com.pettranslator.cattranslator.catsounds.ui.music.SongFragment.Companion.SONGS
import com.pettranslator.cattranslator.catsounds.ui.record.RecordActivity
import com.pettranslator.cattranslator.catsounds.ui.song.PlaySongActivity
import com.pettranslator.cattranslator.catsounds.utils.AnalyticsHelper
import com.pettranslator.cattranslator.catsounds.utils.ScreenName
import com.pettranslator.cattranslator.catsounds.utils.ad.AdManager
import com.pettranslator.cattranslator.catsounds.utils.isInternetConnected
import com.pettranslator.cattranslator.catsounds.utils.openActivity
import com.pettranslator.cattranslator.catsounds.utils.setSafeOnClickListener
import com.pettranslator.cattranslator.catsounds.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class TranslateFragment : BaseFragment<FragmentTranslateBinding>() {

    @Inject
    lateinit var adManager: AdManager

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper
    @Inject
    lateinit var appContainer: AppContainer
    override fun inflateViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentTranslateBinding = FragmentTranslateBinding.inflate(inflater)

    override fun initialize() {

        analyticsHelper.logScreenView(ScreenName.TRANSLATE)

        adManager.loadNativeClickAd(viewBinding.adContainer, onAdLoaded = {
            analyticsHelper.logShowNative(ScreenName.TRANSLATE)
        }, onAdFailed = {
            analyticsHelper.logShowNativeFailed(ScreenName.TRANSLATE)
        }, onAdImpression = {
            analyticsHelper.logAdImpression("native", BuildConfig.NATIVE_AD_UNIT_ID)
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
        lifecycleScope.launch {
            val isOnline = isInternetConnected(requireContext())
            if (!isOnline) {
                requireActivity().showToast(getString(R.string.connect_internet))
                return@launch
            } else {
                typeTrans = type
                requireContext().openActivity(RecordActivity::class.java)
            }
        }




    }


    companion object {
        var typeTrans = ETypeTranslator.HUMANTOANIMAL
    }

}