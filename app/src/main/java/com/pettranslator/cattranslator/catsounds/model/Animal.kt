package com.pettranslator.cattranslator.catsounds.model

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes

data class Animal(
    val type: EAnimal,
    val soundResId: String,
    @DrawableRes val imageResId: Int,
    val description: String,
    val resultTranslate: String = ""
)