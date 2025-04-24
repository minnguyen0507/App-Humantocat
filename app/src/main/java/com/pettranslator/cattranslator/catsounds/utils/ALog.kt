package com.pettranslator.cattranslator.catsounds.utils

import android.util.Log
import com.pettranslator.cattranslator.catsounds.BuildConfig

object ALog {

    fun d(tag: String, content: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, content)
        }
    }

}