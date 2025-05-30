package com.pettranslator.cattranslator.catsounds.ui.music

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.pettranslator.cattranslator.catsounds.BuildConfig
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.AppContainer
import com.pettranslator.cattranslator.catsounds.bases.fragment.BaseFragment
import com.pettranslator.cattranslator.catsounds.databinding.FragmentMusicBinding
import com.pettranslator.cattranslator.catsounds.model.ETypeSong
import com.pettranslator.cattranslator.catsounds.model.Song
import com.pettranslator.cattranslator.catsounds.ui.song.PlaySongActivity
import com.pettranslator.cattranslator.catsounds.utils.ALog
import com.pettranslator.cattranslator.catsounds.utils.AnalyticsHelper
import com.pettranslator.cattranslator.catsounds.utils.DataProvider
import com.pettranslator.cattranslator.catsounds.utils.ScreenName
import com.pettranslator.cattranslator.catsounds.utils.SharedPref
import com.pettranslator.cattranslator.catsounds.utils.ad.AdManager
import com.pettranslator.cattranslator.catsounds.utils.isInternetConnected
import com.pettranslator.cattranslator.catsounds.utils.openActivity
import com.pettranslator.cattranslator.catsounds.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SongFragment : BaseFragment<FragmentMusicBinding>() {

    private var isShowToolbar = true

    @Inject
    lateinit var dataProvider: DataProvider

    private lateinit var songAdapter: SongAdapter

    @Inject
    lateinit var adManager: AdManager

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper
    @Inject
    lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isShowToolbar = arguments?.getBoolean(ARG_TOOLBAR) != false
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentMusicBinding = FragmentMusicBinding.inflate(inflater)

    override fun initialize() {
        songAdapter = SongAdapter()
        viewBinding.toolbar.root.visibility = if (isShowToolbar) View.VISIBLE else View.GONE
        viewBinding.rcvMusic.adapter = songAdapter

        analyticsHelper.logScreenView(ScreenName.SONG)

        songAdapter.registerItemClickListener { view, song, position ->
            when (view.id) {
                R.id.layoutSong -> {
                    playSong(position)
                }

                R.id.imvStar -> {
                    saveFavoriteSong(song, position)
                }
            }

        }

    }

    private fun saveFavoriteSong(
        song: Song,
        position: Int
    ) {
        song.isFavorite = !song.isFavorite
        sharedPref.toggleFavorite(song.filename)
        songAdapter.notifyItemChanged(position)
    }

    private fun playSong(position: Int) {

        if (!requireActivity().isInternetConnected()) {
            requireActivity().showToast(getString(R.string.connect_internet))
            return
        }

        requireContext().openActivity(PlaySongActivity::class.java) {
            putSerializable(SONGS, ArrayList(getSongs()))  // nếu songs là List<Song>
            putInt(CURRENT_INDEX, position)
        }
    }

    override fun onResume() {
        super.onResume()
        var songs = getSongs()
        try {
            val favorites = sharedPref.getFavorites()
            ALog.d("MusicFragment", "favorites: $favorites")
            songs = songs.map { it.copy(isFavorite = favorites.contains(it.filename.trim())) }
            ALog.d("MusicFragment", "songs: $songs")
        } catch (e: Exception) {
            ALog.d("SongFragment", "onResume: ${e.message}")
        }

        songAdapter.addData(songs.toMutableList())

        adManager.showInterstitialAdIfEligible(
            requireActivity(),
            minIntervalMillis = appContainer.adConfig?.interDelaySongsSec?.times(1000L) ?: 30_000L,
            adTag = "Song",
            onAdClosed = {
                dismissAdLoadingDialog()
            },
            onAdSkipped = {
                dismissAdLoadingDialog()
            },
            onAdFailedToShow = {
                dismissAdLoadingDialog()
            },
            onAdStartShowing = {
                showAdLoadingDialog()
            }, onAdImpression = {
                analyticsHelper.logAdImpression("interstitial", BuildConfig.INTERSTITIAL_AD_UNIT_ID)
            })
    }

    private fun getSongs(): List<Song> {
        var type = ETypeSong.Music
        if (!isShowToolbar) {
            type = ETypeSong.Challenge
        }
        return dataProvider.getCatSongs().filter { it.type == type }
    }

    companion object {
        private const val ARG_TOOLBAR = "toolbar"
        const val SONGS = "songs"
        const val CURRENT_INDEX = "currentIndex"

        fun newInstance(toolbar: Boolean): SongFragment {
            val fragment = SongFragment()
            fragment.arguments = bundleOf(ARG_TOOLBAR to toolbar)
            return fragment
        }
    }
}