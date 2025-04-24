package com.pettranslator.cattranslator.catsounds.ui.game

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.ViewPagerAdapter
import com.pettranslator.cattranslator.catsounds.bases.fragment.BaseFragment
import com.pettranslator.cattranslator.catsounds.databinding.FragmentGameBinding
import com.pettranslator.cattranslator.catsounds.ui.music.MusicFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GameFragment : BaseFragment<FragmentGameBinding>() {

    private lateinit var viewPage: ViewPagerAdapter
    override fun inflateViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentGameBinding = FragmentGameBinding.inflate(inflater)

    override fun initialize() {
        viewPage = ViewPagerAdapter(childFragmentManager, lifecycle)
        viewPage.add(0) { ClickMouseFragment() }
        viewPage.add(1) { MusicFragment.newInstance(false) }
        viewBinding.viewpageGame.adapter = viewPage
        viewBinding.viewpageGame.offscreenPageLimit = 1
        viewBinding.viewpageGame.isUserInputEnabled = true
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