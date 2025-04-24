package com.pettranslator.cattranslator.catsounds.bases.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.pettranslator.cattranslator.catsounds.bases.BaseActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheet<viewBinding : ViewBinding> : BottomSheetDialogFragment() {

    private var _viewBinding: viewBinding? = null
    private val viewBinding get() = _viewBinding!!

    abstract fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): viewBinding

    protected abstract fun initialize()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = inflateViewBinding(inflater, container)
        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        progressDialog = ProgressDialog(requireContext())
        initialize()
    }


    override fun onStart() {
        super.onStart()
        dialog?.let {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            it.window?.setLayout(width, height)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }


    private fun showLoading(isShow: Boolean) {
        (activity as? BaseActivity<*>)?.showLoading(isShow)
    }

}