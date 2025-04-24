package com.pettranslator.cattranslator.catsounds.ui.music

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.fragment.BaseFragment
import com.pettranslator.cattranslator.catsounds.databinding.FragmentMusicBinding
import com.pettranslator.cattranslator.catsounds.model.ETypeSong
import com.pettranslator.cattranslator.catsounds.model.Song
import com.pettranslator.cattranslator.catsounds.ui.song.PlaySongActivity
import com.pettranslator.cattranslator.catsounds.utils.ALog
import com.pettranslator.cattranslator.catsounds.utils.DataProvider
import com.pettranslator.cattranslator.catsounds.utils.SharedPref
import com.pettranslator.cattranslator.catsounds.utils.ad.AdManager
import com.pettranslator.cattranslator.catsounds.utils.isInternetConnected
import com.pettranslator.cattranslator.catsounds.utils.openActivity
import com.pettranslator.cattranslator.catsounds.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MusicFragment : BaseFragment<FragmentMusicBinding>() {
    private var isShowToolbar = true

    @Inject
    lateinit var dataProvider: DataProvider
    private lateinit var songAdapter: SongAdapter

    @Inject
    lateinit var adManager: AdManager

    @Inject
    lateinit var sharedPref: SharedPref

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

        songAdapter.registerItemClickListener { view, song, position ->
            when (view.id) {
                R.id.layoutSong -> {
                    val remainingUses = sharedPref.loadRemainingUses()

                    if (remainingUses > 0) {
                        requireContext().openActivity(PlaySongActivity::class.java) {
                            putSerializable(
                                SONGS,
                                ArrayList(getSongs())
                            )  // nếu songs là List<Song>
                            putInt(CURRENT_INDEX, position)
                        }
                        sharedPref.saveRemainingUses(remainingUses - 1)
                    } else {
                        if (!requireActivity().isInternetConnected()) {
                            requireActivity().showToast(getString(R.string.connect_internet))
                            return@registerItemClickListener
                        }
                        adManager.showInterstitialAd(requireActivity(), onAdClosed = {
                            requireContext().openActivity(PlaySongActivity::class.java) {
                                putSerializable(
                                    SONGS,
                                    ArrayList(getSongs())
                                )  // nếu songs là List<Song>
                                putInt(CURRENT_INDEX, position)
                            }
                        }, onAdFailed = { errorMessage ->
                            {
                                requireActivity().showToast(getString(R.string.connect_internet))
                            }
                        })
                    }

                }

                R.id.imvStar -> {
                    song.isFavorite = !song.isFavorite
                    sharedPref.toggleFavorite(song.filename)
                    songAdapter.notifyItemChanged(position)
                }
            }

        }

    }

    override fun onResume() {
        super.onResume()
        var songs = getSongs()
        val favorites = sharedPref.getFavorites()
        ALog.d("MusicFragment", "favorites: $favorites")
        songs = songs.map { it.copy(isFavorite = favorites.contains(it.filename.trim())) }
        ALog.d("MusicFragment", "songs: $songs")

        songAdapter.addData(songs.toMutableList())
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

        fun newInstance(toolbar: Boolean): MusicFragment {
            val fragment = MusicFragment()
            fragment.arguments = bundleOf(ARG_TOOLBAR to toolbar)
            return fragment
        }
    }
}