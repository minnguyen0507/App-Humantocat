package com.pettranslator.cattranslator.catsounds.ui.setting

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.fragment.BaseFragment
import com.pettranslator.cattranslator.catsounds.databinding.FragmentSettingBinding
import com.pettranslator.cattranslator.catsounds.ui.language.LanguageActivity
import com.pettranslator.cattranslator.catsounds.ui.policy.PolicyActivity
import com.pettranslator.cattranslator.catsounds.utils.AnalyticsHelper
import com.pettranslator.cattranslator.catsounds.utils.DataProvider
import com.pettranslator.cattranslator.catsounds.utils.ScreenName
import com.pettranslator.cattranslator.catsounds.utils.SharedPref
import com.pettranslator.cattranslator.catsounds.utils.openActivity
import com.pettranslator.cattranslator.catsounds.utils.rateUs
import com.pettranslator.cattranslator.catsounds.utils.share
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>() {

    @Inject
    lateinit var dataProvider: DataProvider

    @Inject
    lateinit var sharedPref: SharedPref

    private lateinit var settingAdapter: SettingAdapter

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    override fun inflateViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentSettingBinding = FragmentSettingBinding.inflate(inflater)

    override fun initialize() {
        settingAdapter = SettingAdapter()
        viewBinding.rcvSetting.adapter = settingAdapter
        viewBinding.rcvSetting.setHasFixedSize(true)

        analyticsHelper.logScreenView(ScreenName.SETTING)

        settingAdapter.registerItemClickListener { view, animal, postion ->
            when (animal.imageResId) {
                R.drawable.ngonngu_2 -> {
                    requireContext().openActivity(LanguageActivity::class.java)
                }

                R.drawable.share_2 -> {
                    requireContext().share("https://play.google.com/store/apps/details?id=${requireContext().packageName}")
                }

                R.drawable.danhgia_2 -> {
                    val dialog = RatingDialog(requireActivity()) { rating ->
                        sharedPref.setFirstSendRate(false)
                        requireContext().rateUs()
                    }
                    dialog.show()
                }

                R.drawable.nhanxet_2 -> {
                    sendFeedbackEmail()
                }

                R.drawable.baomat_2 -> {
                    requireContext().openActivity(PolicyActivity::class.java)
                }
            }
        }

    }

    private fun sendFeedbackEmail() {
        val mail = "hotro@boomstudio.vn"
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$mail")  // Sử dụng mailto: cho đúng định dạng
        }
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.feedback)))
        } catch (e: ActivityNotFoundException) {

        }
    }

    override fun onResume() {
        super.onResume()
        loadSettings()
    }

    private fun loadSettings() {
        val settingsList = dataProvider.getSettings()
        settingAdapter.clearData(true)
        settingAdapter.addData(settingsList.toMutableList())
    }

}