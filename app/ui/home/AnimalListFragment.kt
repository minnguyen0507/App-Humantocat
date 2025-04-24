package com.pettranslator.cattranslator.catsounds.ui.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.fragment.BaseFragment
import com.pettranslator.cattranslator.catsounds.databinding.FragmentListAnimalBinding
import com.pettranslator.cattranslator.catsounds.model.EAnimal
import com.pettranslator.cattranslator.catsounds.utils.DataProvider
import com.pettranslator.cattranslator.catsounds.utils.MediaSoundPlayer
import com.pettranslator.cattranslator.catsounds.utils.SharedPref
import com.pettranslator.cattranslator.catsounds.utils.ad.AdManager
import com.pettranslator.cattranslator.catsounds.utils.isInternetConnected
import com.pettranslator.cattranslator.catsounds.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AnimalListFragment :
    BaseFragment<FragmentListAnimalBinding>() {

    private var filterType: Int = 0

    @Inject
    lateinit var soundPlayer: MediaSoundPlayer

    @Inject
    lateinit var adManager: AdManager

    @Inject
    lateinit var dataProvider: DataProvider

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filterType = arguments?.getInt(ARG_FILTER) ?: 0
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentListAnimalBinding = FragmentListAnimalBinding.inflate(inflater)

    override fun initialize() {
        val adapter = AnimalAdapter()
        viewBinding.rcvAnimal.adapter = adapter
        adapter.registerItemClickListener { view, animal, postion ->
            val remainingUses = sharedPref.loadRemainingUses()
            if (remainingUses > 0) {
                soundPlayer.playFromAsset(animal.soundResId)
                zoomInOutObject(view)
                sharedPref.saveRemainingUses(remainingUses - 1)
            } else {
                if (!requireActivity().isInternetConnected()) {
                    requireActivity().showToast(getString(R.string.connect_internet))
                    return@registerItemClickListener
                }
                adManager.showInterstitialAd(requireActivity(), onAdClosed = {
                    sharedPref.saveRemainingUses(2)
                    soundPlayer.playFromAsset(animal.soundResId)
                    zoomInOutObject(view)
                }, onAdFailed = { _ ->
                    requireActivity().showToast(getString(R.string.connect_internet))
                 })
            }
        }
        if (filterType == EAnimal.CAT.id) {
            adapter.addData(dataProvider.getCatSounds().toMutableList())
            return
        }
        adapter.addData(dataProvider.getDogSounds().toMutableList())
    }

    private fun zoomInOutObject(view: View) {
        val scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.2f)
        val scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.2f)
        val scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1.2f, 1f)
        val scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1.2f, 1f)

        val animatorSet = AnimatorSet()
        animatorSet.play(scaleUpX).with(scaleUpY)
        animatorSet.play(scaleDownX).with(scaleDownY).after(scaleUpX)

        animatorSet.duration = 350
        animatorSet.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        soundPlayer.release()
    }

    companion object {
        private const val ARG_FILTER = "filter"

        fun newInstance(filter: Int): AnimalListFragment {
            val fragment = AnimalListFragment()
            fragment.arguments = bundleOf(ARG_FILTER to filter)
            return fragment
        }
    }
}