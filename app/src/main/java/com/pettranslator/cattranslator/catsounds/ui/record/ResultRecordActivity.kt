package com.pettranslator.cattranslator.catsounds.ui.record

import android.os.CountDownTimer
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.BaseActivity
import com.pettranslator.cattranslator.catsounds.databinding.ActivityResultRecordBinding
import com.pettranslator.cattranslator.catsounds.model.EAnimal
import com.pettranslator.cattranslator.catsounds.model.EPlayMediaState
import com.pettranslator.cattranslator.catsounds.utils.ALog
import com.pettranslator.cattranslator.catsounds.utils.MediaSoundPlayer
import com.pettranslator.cattranslator.catsounds.utils.ad.AdManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ResultRecordActivity : BaseActivity<ActivityResultRecordBinding>() {
    @Inject
    lateinit var adManager: AdManager
    private var countDownTimer: CountDownTimer? = null
    private var seconds = 0
    var currentSeconds = 0
    private var playPauseState = EPlayMediaState.STOPPED
    private var typeAnimal = EAnimal.CAT


    @Inject
    lateinit var soundPlayer: MediaSoundPlayer

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityResultRecordBinding =
        ActivityResultRecordBinding.inflate(inflater)

    override fun initialize() {
        enableEdgeToEdge()
        seconds = intent.getIntExtra(RecordActivity.SECONDS, 0)
        ALog.d("ResultRecordActivitySSS", "seconds: $seconds")

        adManager.loadNativeClickAd(viewBinding.adContainer, onAdLoaded = {}, onAdFailed = {})
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        viewBinding.apply {
            btnBack.setOnClickListener {
                finish()
            }
            btnPlay.setOnClickListener {
                val assetManager = this@ResultRecordActivity.assets
                var folder = ""
                folder = if (typeAnimal == EAnimal.CAT) {
                    "cat_sound_translator"
                } else {
                    "dog_sound"
                }
                val fileList = assetManager.list(folder) ?: emptyArray()
                val randomFile = fileList.random()
                togglePlayPause("$folder/$randomFile")
            }
            imvCat.setOnClickListener {
                typeAnimal = EAnimal.CAT
                it.background = ContextCompat.getDrawable(this@ResultRecordActivity, R.drawable.circle_button_border)
                viewBinding.imvDog.background =
                    ContextCompat.getDrawable(this@ResultRecordActivity, R.drawable.bg_circle_unselected)
            }
            imvDog.setOnClickListener {
                typeAnimal = EAnimal.DOG
                it.background = ContextCompat.getDrawable(this@ResultRecordActivity, R.drawable.circle_button_border)
                viewBinding.imvCat.background =
                    ContextCompat.getDrawable(this@ResultRecordActivity, R.drawable.bg_circle_unselected)
            }
        }
    }

    private fun togglePlayPause(fileName: String) {
        ALog.d("ResultRecordActivitySSS", "playPauseState: $playPauseState")
        when (playPauseState) {
            EPlayMediaState.PAUSED -> {
                soundPlayer.resume()
                playPauseState = EPlayMediaState.PLAYING
                viewBinding.btnPlay.setImageResource(R.drawable.pause)
            }

            EPlayMediaState.PLAYING -> {
                soundPlayer.pause()
                playPauseState = EPlayMediaState.PAUSED
                viewBinding.btnPlay.setImageResource(R.drawable.play_1)
            }

            EPlayMediaState.STOPPED -> {
                soundPlayer.playFromAsset(fileName, isLoop = true)
                playPauseState = EPlayMediaState.PLAYING
                viewBinding.btnPlay.setImageResource(R.drawable.pause)
            }

        }
        toggleClock(viewBinding.textView7, seconds)
        if (currentSeconds == seconds) {
            soundPlayer.stop()
        }
    }

    fun toggleClock(textView: TextView, maxSeconds: Int) {
        if (playPauseState == EPlayMediaState.PLAYING) {
            countDownTimer = object : CountDownTimer((maxSeconds - currentSeconds) * 1000L, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    currentSeconds++
                    val minutes = currentSeconds / 60
                    val seconds = currentSeconds % 60
                    textView.text = String.format("%02d:%02d", minutes, seconds)
                }

                override fun onFinish() {
                    playPauseState = EPlayMediaState.STOPPED
                    currentSeconds = 0
                    viewBinding.btnPlay.setImageResource(R.drawable.play_1)
                    soundPlayer.stop()
                }
            }.start()
        } else {
            countDownTimer?.cancel()

        }

    }


    override fun onDestroy() {
        super.onDestroy()
        soundPlayer.release()
    }

}