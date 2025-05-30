package com.pettranslator.cattranslator.catsounds.model

import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import org.json.JSONObject

object AdsConfigParser {
    fun parseFromRemoteConfigValues(allValues: Map<String, FirebaseRemoteConfigValue>): AdConfig {
        return AdConfig(
            interDelayHomeSec = allValues["inter_delay_home_sec"]?.asLong()?.toInt() ?: 60,
            interDelayRecordSec = allValues["inter_delay_record_sec"]?.asLong()?.toInt() ?: 90,
            interDelayPlaybackSec = allValues["inter_delay_playback_sec"]?.asLong()?.toInt() ?: 45,
            interDelayCatSoundSec = allValues["inter_delay_cat_sound_sec"]?.asLong()?.toInt() ?: 25,
            interDelayTranslateSec = allValues["inter_delay_translate_sec"]?.asLong()?.toInt() ?: 30,
            interDelayGameSec = allValues["inter_delay_game_sec"]?.asLong()?.toInt() ?: 30,
            interDelaySongsSec = allValues["inter_delay_songs_sec"]?.asLong()?.toInt() ?: 30,
            bannerEnabled = allValues["banner_enabled"]?.asBoolean() ?: true,
            rewardedEnabled = allValues["rewarded_enabled"]?.asBoolean() ?: true,
            rewardedMaxPerDay = allValues["rewarded_max_per_day"]?.asLong()?.toInt() ?: 5
        )
    }
}