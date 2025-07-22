package com.pettranslator.cattranslator.catsounds.model

import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import com.google.gson.Gson
import com.pettranslator.cattranslator.catsounds.utils.ALog
import org.json.JSONObject

object AdsConfigParser {
    fun parseFromRemoteConfigValues(allValues: Map<String, FirebaseRemoteConfigValue>): AdConfig {
        return AdConfig(
            interDelayHomeSec = allValues["inter_delay_home_sec"]?.asString()?.toIntOrNull() ?: 60,
            interDelayRecordSec = allValues["inter_delay_record_sec"]?.asString()?.toIntOrNull() ?: 90,
            interDelayPlaybackSec = allValues["inter_delay_playback_sec"]?.asString()?.toIntOrNull() ?: 45,
            interDelayCatSoundSec = allValues["inter_delay_cat_sound_sec"]?.asString()?.toIntOrNull() ?: 25,
            interDelayTranslateSec = allValues["inter_delay_translate_sec"]?.asString()?.toIntOrNull() ?: 30,
            interDelayGameSec = allValues["inter_delay_game_sec"]?.asString()?.toIntOrNull() ?: 30,
            interDelaySongsSec = allValues["inter_delay_songs_sec"]?.asString()?.toIntOrNull() ?: 30,
            bannerEnabled = allValues["banner_enabled"]?.asString()?.toBooleanStrictOrNull() ?: true,
            rewardedEnabled = allValues["rewarded_enabled"]?.asString()?.toBooleanStrictOrNull() ?: true,
            rewardedMaxPerDay = allValues["rewarded_max_per_day"]?.asString()?.toIntOrNull() ?: 5,
            interEnabled = allValues["inter_enabled"]?.asString()?.toBooleanStrictOrNull() ?:true
        )
    }
}