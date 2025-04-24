package com.pettranslator.cattranslator.catsounds.ui.setting

import android.app.Activity
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.BaseDialog
import com.pettranslator.cattranslator.catsounds.databinding.DialogRatingBinding

class RatingDialog(
    activity: Activity,
    private val onSubmit: (Float) -> Unit
) : BaseDialog<DialogRatingBinding>(activity, canAble = true) {

    override fun getContentView(): Int = R.layout.dialog_rating

    override fun setLanguage() {
        // Nếu cần xử lý ngôn ngữ (đa ngữ), bạn có thể xử lý ở đây
    }

    override fun initView() {
        // Setup UI nếu cần
    }

    override fun bindView() {
        dataBinding.btnSubmit.setOnClickListener {
            val rating = dataBinding.ratingBar.rating
            onSubmit(rating)
            dismiss()
        }
    }
}
