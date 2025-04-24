package com.pettranslator.cattranslator.catsounds.ui.record

import android.app.Activity
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.BaseDialog
import com.pettranslator.cattranslator.catsounds.databinding.DialogTranslationResultBinding
import com.pettranslator.cattranslator.catsounds.model.Animal
import com.pettranslator.cattranslator.catsounds.utils.setSafeOnClickListener

class TranslateResultDialog(
    activity: Activity,
    private val animal: Animal,
    private val onSubmit: (Float) -> Unit
) : BaseDialog<DialogTranslationResultBinding>(activity, canAble = true) {

    override fun getContentView(): Int = R.layout.dialog_translation_result

    override fun setLanguage() {
        // Nếu cần xử lý ngôn ngữ (đa ngữ), bạn có thể xử lý ở đây
    }

    override fun initView() {
        dataBinding.animal = animal
        dataBinding.btnClose.setSafeOnClickListener {
            dismiss()
        }
    }

    override fun bindView() {

    }
}