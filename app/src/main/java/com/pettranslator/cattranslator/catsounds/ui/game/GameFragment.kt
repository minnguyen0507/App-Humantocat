package com.pettranslator.cattranslator.catsounds.ui.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.viewpager2.widget.ViewPager2
import com.pettranslator.cattranslator.catsounds.BuildConfig
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.AppContainer
import com.pettranslator.cattranslator.catsounds.bases.ViewPagerAdapter
import com.pettranslator.cattranslator.catsounds.bases.fragment.BaseFragment
import com.pettranslator.cattranslator.catsounds.databinding.FragmentGameBinding
import com.pettranslator.cattranslator.catsounds.ui.music.SongFragment
import com.pettranslator.cattranslator.catsounds.utils.AnalyticsHelper
import com.pettranslator.cattranslator.catsounds.utils.ScreenName
import com.pettranslator.cattranslator.catsounds.utils.ad.AdManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class GameFragment : BaseFragment<FragmentGameBinding>() {

    private lateinit var viewPage: ViewPagerAdapter

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    @Inject
    lateinit var adManager: AdManager

    @Inject
    lateinit var appContainer: AppContainer

    override fun inflateViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentGameBinding = FragmentGameBinding.inflate(inflater)

    override fun initialize() {

        analyticsHelper.logScreenView(ScreenName.GAME)

        viewPage = ViewPagerAdapter(childFragmentManager, lifecycle)
        viewPage.add(ClickMouseFragment())
        viewPage.add(SongFragment.newInstance(false))

        viewBinding.viewpageGame.apply {
            adapter = viewPage
            offscreenPageLimit = 1
            isUserInputEnabled = true
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when (position) {
                        0 -> {
                            updateBackgroundButton(viewBinding.btnClickMouse, viewBinding.btnMusic)
                        }
                        1 -> {
                            updateBackgroundButton(viewBinding.btnMusic, viewBinding.btnClickMouse)
                        }
                    }
                }
            })
        }

        viewBinding.apply {
            btnClickMouse.setOnClickListener {
                viewpageGame.setCurrentItem(0, true)
                updateBackgroundButton(btnClickMouse, btnMusic)
            }
            btnMusic.setOnClickListener {
                viewpageGame.setCurrentItem(1, true)
                updateBackgroundButton(btnMusic, btnClickMouse)
            }

        }
    }

    fun updateBackgroundButton(view: AppCompatButton, viewUnActive: AppCompatButton) {
        view.setTextAppearance(R.style.tab_style_active)
        view.setBackgroundResource(R.drawable.bg_tab_active)
        viewUnActive.setTextAppearance(R.style.tab_style_inactive)
        viewUnActive.setBackgroundResource(R.drawable.bg_tab_inactive)
    }

}