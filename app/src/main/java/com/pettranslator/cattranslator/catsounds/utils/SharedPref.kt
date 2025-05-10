package com.pettranslator.cattranslator.catsounds.utils

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class SharedPref @Inject constructor(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "cat_preferences"
        private const val KEY_LANGUAGE = "language"
        private const val KEY_FIRST_RUN = "first_run"
        private const val KEY_FIRST_LANGUAGE = "first_language"
        private const val KEY_SEND_RATE = "send_rate"
        private const val KEY_REMAINING_USES = "remaining_uses"
        private const val KEY_FAVORITE_SONGS = "favorite_songs"
        private const val KEY_APP_VERSION = "app_version"
    }

    fun saveVersion(version: String) {
        sharedPreferences.edit { putString(KEY_APP_VERSION, version) }
    }

    fun getVersion(): String {
        return sharedPreferences.getString(KEY_APP_VERSION, "").toString()
    }

    fun saveLanguage(languageCode: String) {
        sharedPreferences.edit { putString(KEY_LANGUAGE, languageCode) }
    }

    fun getLanguage(): String? {
        return sharedPreferences.getString(KEY_LANGUAGE, "en")
    }

    fun setFirstRun(isFirstRun: Boolean) {
        sharedPreferences.edit() { putBoolean(KEY_FIRST_RUN, isFirstRun) }
    }

    fun getFirstRun(): Boolean {
        return sharedPreferences.getBoolean(KEY_FIRST_RUN, true)
    }

    fun setFirstLanguage(isFirstLanguage: Boolean) {
        sharedPreferences.edit() { putBoolean(KEY_FIRST_LANGUAGE, isFirstLanguage) }
    }

    fun getFirstLanguage(): Boolean {
        return sharedPreferences.getBoolean(KEY_FIRST_LANGUAGE, true)
    }

    fun setFirstSendRate(isFirstRate: Boolean) {
        sharedPreferences.edit() { putBoolean(KEY_SEND_RATE, isFirstRate) }
    }

    fun loadRemainingUses(): Int {
        return sharedPreferences.getInt(KEY_REMAINING_USES, 0)
    }

    fun saveRemainingUses(count: Int) {
        sharedPreferences.edit() { putInt(KEY_REMAINING_USES, count) }
    }

    fun toggleFavorite(songId: String) {
        val favorites = sharedPreferences.getStringSet(KEY_FAVORITE_SONGS, mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        if (favorites.contains(songId)) {
            favorites.remove(songId)
        } else {
            favorites.add(songId)
        }
        sharedPreferences.edit() { putStringSet(KEY_FAVORITE_SONGS, favorites) }
    }

    fun getFavorites(): Set<String> {
        return sharedPreferences.getStringSet(KEY_FAVORITE_SONGS, emptySet()) ?: emptySet()
    }
}
