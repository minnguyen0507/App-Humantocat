package com.pettranslator.cattranslator.catsounds.model

import androidx.annotation.DrawableRes

data class IntroSlide(
    @DrawableRes val imageRes: Int,
    val title: String,
    val description: String
)