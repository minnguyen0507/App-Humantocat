package com.pettranslator.cattranslator.catsounds.model

data class LanguageItem(
    val name: String,
    val flagResId: Int,
    val localeCode: String,
    var isSelected: Boolean = false
)
