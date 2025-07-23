package com.pettranslator.cattranslator.catsounds.model

import com.google.gson.Gson

data class AdConfig(
    val interDelayHomeSec: Int,
    val interDelayRecordSec: Int,
    val interDelayPlaybackSec: Int,
    val interDelayCatSoundSec: Int,
    val interDelayTranslateSec: Int,
    val interDelayGameSec: Int,
    val interDelaySongsSec: Int,
    val bannerEnabled: Boolean,
    val rewardedEnabled: Boolean,
    val rewardedMaxPerDay: Int,
    val interEnabledSplash: Boolean,
    val interEnabledHome: Boolean
){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}

