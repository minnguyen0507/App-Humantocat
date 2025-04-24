package com.pettranslator.cattranslator.catsounds.bases

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

import com.pettranslator.cattranslator.catsounds.R

abstract class BaseDialog<DB : ViewDataBinding>(activity: Activity, private var canAble: Boolean) :
    Dialog(activity, R.style.BaseDialog) {

    lateinit var dataBinding: DB

    abstract fun getContentView(): Int
    abstract fun setLanguage()
    abstract fun initView()
    abstract fun bindView()
    override fun onStart() {
        super.onStart()
        window?.setLayout(
            (context.resources.displayMetrics.widthPixels * 0.9).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setLanguage()
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        dataBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context), getContentView(), null, false)
        setContentView(dataBinding.root)
        setCancelable(canAble)
        initView()
        bindView()
    }

}