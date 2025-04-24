package com.pettranslator.cattranslator.catsounds.model

import com.google.gson.Gson
import java.io.Serializable

data class Song(
    val title: String,
    val filename: String,
    var isFavorite: Boolean = false,
    var type: ETypeSong = ETypeSong.Music,
): Serializable{
    override fun toString(): String {
        return Gson().toJson(this)
    }
}
