package com.pettranslator.cattranslator.catsounds.ui.song

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.BaseActivity
import com.pettranslator.cattranslator.catsounds.databinding.ActivityPlaySongBinding
import com.pettranslator.cattranslator.catsounds.model.Song
import com.pettranslator.cattranslator.catsounds.ui.music.SongFragment
import com.pettranslator.cattranslator.catsounds.utils.AnalyticsHelper
import com.pettranslator.cattranslator.catsounds.utils.MediaSoundPlayer
import com.pettranslator.cattranslator.catsounds.utils.ScreenName
import com.pettranslator.cattranslator.catsounds.utils.ad.AdManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaySongActivity : BaseActivity<ActivityPlaySongBinding>() {

    @Inject
    lateinit var adManager: AdManager

    @Inject
    lateinit var soundPlayer: MediaSoundPlayer

    private var currentIndex = 0

    private lateinit var songs: List<Song>

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private val updateSeekBarRunnable = object : Runnable {
        override fun run() {
            val currentPosition = soundPlayer.getCurrentPosition()
            viewBinding.seekBar.progress = currentPosition
            viewBinding.tvStartTime.text = formatTime(currentPosition)
            handler.postDelayed(this, 500)
        }
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityPlaySongBinding =
        ActivityPlaySongBinding.inflate(inflater)

    override fun initialize() {
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        adManager.loadNativeClickAd(viewBinding.adContainer, onAdLoaded = {
            analyticsHelper.logShowNative(ScreenName.SONG_PLAYING)
        }, onAdFailed = {
            analyticsHelper.logShowNativeFailed(ScreenName.SONG_PLAYING)
        })
        setupUI()
        playCurrentSong()
    }

    private fun setupUI() {

        songs = intent.getSerializableExtra(SongFragment.SONGS) as List<Song>
        currentIndex = intent.getIntExtra(SongFragment.CURRENT_INDEX, 0)

        checkButtonNext()
        viewBinding.btnPlayPause.setOnClickListener {
            if (soundPlayer.isPlaying()) {
                soundPlayer.pause()
                viewBinding.btnPlayPause.setImageResource(R.drawable.play_2)
            } else {
                soundPlayer.resume()
                viewBinding.btnPlayPause.setImageResource(R.drawable.pause_2)
                handler.post(updateSeekBarRunnable)
            }
        }

        viewBinding.btnNext.setOnClickListener {
            if (currentIndex < songs.size - 1) {
                currentIndex++
                playCurrentSong()
            }
        }

        viewBinding.btnBack.setOnClickListener {
            finish()
        }

        viewBinding.btnPrevious.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                playCurrentSong()
            }
        }

        viewBinding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    soundPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })
    }

    private fun checkButtonNext() {
        val tintColor = ContextCompat.getColor(this, R.color.cardViewProcess)
        if (currentIndex == 0) {
            viewBinding.btnPrevious.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
        } else {
            viewBinding.btnPrevious.clearColorFilter()
        }

        if (currentIndex == songs.size - 1){
            viewBinding.btnNext.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
        } else {
            viewBinding.btnNext.clearColorFilter()
        }
    }
    private fun playCurrentSong() {
        checkButtonNext()
        val song = songs[currentIndex]
        viewBinding.tvNameSong.text = song.title
        viewBinding.btnPlayPause.setImageResource(R.drawable.pause_2)
        soundPlayer.playFromAsset(song.filename)
        val duration = soundPlayer.getDuration()
        viewBinding.tvEndTime.text = formatTime(duration)
        handler.postDelayed({
            viewBinding.seekBar.max = duration
            handler.post(updateSeekBarRunnable)

        }, 300)
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPlayer.release()
        handler.removeCallbacks(updateSeekBarRunnable)
    }

    override fun onResume() {
        super.onResume()
        analyticsHelper.logScreenView(ScreenName.SONG_PLAYING)
    }

    private fun formatTime(ms: Int): String {
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}