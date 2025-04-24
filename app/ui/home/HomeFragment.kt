package com.pettranslator.cattranslator.catsounds.ui.home

import android.media.SoundPool
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.ViewPagerAdapter
import com.pettranslator.cattranslator.catsounds.bases.fragment.BaseFragment
import com.pettranslator.cattranslator.catsounds.databinding.FragmentHomeBinding
import com.pettranslator.cattranslator.catsounds.model.EAnimal
import com.pettranslator.cattranslator.catsounds.utils.ALog
import com.pettranslator.cattranslator.catsounds.utils.DataProvider
import com.pettranslator.cattranslator.catsounds.utils.ad.AdManager
import com.pettranslator.cattranslator.catsounds.utils.setSafeOnClickListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private lateinit var viewPage: ViewPagerAdapter

    @Inject
    lateinit var adManager: AdManager

    @Inject lateinit var dataProvider: DataProvider

    override fun inflateViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater)

    override fun initialize() {
        adManager.loadNativeClickAd(viewBinding.adContainer,onAdLoaded = {}, onAdFailed = {})
        viewPage = ViewPagerAdapter(childFragmentManager, lifecycle)
        viewPage.add(0) { AnimalListFragment.newInstance(EAnimal.CAT.id) }
        viewPage.add(1) { AnimalListFragment.newInstance(EAnimal.DOG.id) }
        viewBinding.vpAnimal.adapter = viewPage
        viewBinding.vpAnimal.offscreenPageLimit = 1
        viewBinding.apply {
            btnCat.setOnClickListener{ it ->
                val assetManager = requireContext().assets
                var folder = ""
                folder = "dog_sound"

                val fileList = assetManager.list(folder) ?: emptyArray()
                ALog.d("HomeFragmentSS", "fileList: ${fileList.joinToString(",")}")
                val listDog = arrayListOf<String>()
                dataProvider.getDogSounds().forEach {dog->
                    listDog.add(dog.soundResId)

                }
                ALog.d("HomeFragmentSS", "Dog sound: ${listDog}")
                vpAnimal.setCurrentItem(0, true)
                updateBackgroundButton(btnCat, btnDog)
            }
            btnDog.setOnClickListener{
                vpAnimal.setCurrentItem(1, true)
                updateBackgroundButton(btnDog, btnCat)
            }
            vpAnimal.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when (position) {
                        0 -> {
                            updateBackgroundButton(btnCat, btnDog)
                        }
                        1 -> {
                            updateBackgroundButton(btnDog, btnCat)
                        }
                    }
                }
            })
        }

    }

    fun updateBackgroundButton(view: AppCompatButton, viewUnActive: AppCompatButton) {
        view.setTextAppearance(R.style.tab_style_active)
        view.setBackgroundResource(R.drawable.bg_tab_active)
        viewUnActive.setTextAppearance(R.style.tab_style_inactive)
        viewUnActive.setBackgroundResource(R.drawable.bg_tab_inactive)
    }
}