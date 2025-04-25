package com.pettranslator.cattranslator.catsounds.ui.game

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.fragment.BaseFragment
import com.pettranslator.cattranslator.catsounds.databinding.FragmentClickMounseBinding
import com.pettranslator.cattranslator.catsounds.utils.MediaSoundPlayer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClickMouseFragment :
    BaseFragment<FragmentClickMounseBinding>() {

    var resSound = R.raw.click1_1
    @Inject
    lateinit var soundPlayer: MediaSoundPlayer

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentClickMounseBinding = FragmentClickMounseBinding.inflate(inflater)

    override fun initialize() {
        setUpSpinner()
    }

    private fun setUpSpinner() {
        val items = listOf(
            getString(R.string.sound) + " 1",
            getString(R.string.sound) + " 2",
            getString(R.string.sound) + " 3"
        )

        val adapterSpinner = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            items
        )
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        viewBinding.mySpinner.apply {
            adapter = adapterSpinner
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                ) {
                    when (position) {
                        0 -> resSound = R.raw.click1_1
                        1 -> resSound = R.raw.click2_2
                        2 -> resSound = R.raw.click3_3
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }
        viewBinding.imageView9.setOnClickListener {
            soundPlayer.playRaw(resSound)
        }
    }


    override fun onResume() {
        super.onResume()
        viewBinding.mySpinner.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        soundPlayer.release()
    }

    override fun onPause() {
        super.onPause()
        viewBinding.mySpinner.visibility = View.GONE
    }

}