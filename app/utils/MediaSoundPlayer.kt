package com.pettranslator.cattranslator.catsounds.utils

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.annotation.RawRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaSoundPlayer @Inject constructor(@ApplicationContext private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var onCompletion: (() -> Unit)? = null

    fun playFromAsset(assetFilename: String, onComplete: (() -> Unit)? = null, isLoop: Boolean = false) {
        stop()
        try {
            val afd = context.assets.openFd(assetFilename)
            mediaPlayer = MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                isLooping = isLoop
                prepare()
                start()
                onCompletion = onComplete

                setOnCompletionListener {
                    onCompletion?.invoke()
                    releasePlayer()
                }

                setOnErrorListener { _, _, _ ->
                    releasePlayer()
                    true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun pause() {
        mediaPlayer?.takeIf { it.isPlaying }?.pause()
    }

    fun resume() {
        mediaPlayer?.start()
    }

    fun stop() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) it.stop()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            releasePlayer()
        }
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }

    fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    private fun releasePlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
        onCompletion = null
    }

    fun release() {
        stop()
    }


    fun playRaw(@RawRes rawResId: Int, onComplete: (() -> Unit)? = null) {
        stop()

        try {
            mediaPlayer = MediaPlayer.create(context.applicationContext, rawResId)?.apply {
                setOnCompletionListener {
                    onComplete?.invoke()
                    releasePlayer()
                }
                setOnErrorListener { mp, what, extra ->

                    releasePlayer()
                    true
                }
                start()
            }
        } catch (e: Exception) {

        }
    }


}

