package com.pettranslator.cattranslator.catsounds.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.postDelayed
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.databinding.DialogAdLoadingBinding


class AdLoadingDialogFragment : DialogFragment() {

    private var _binding: DialogAdLoadingBinding? = null
    private val binding get() = _binding!!

    private var timeoutHandler: Handler? = null
    private var timeoutRunnable: Runnable? = null

    var onTimeout: (() -> Unit)? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Không có title bar
        setStyle(STYLE_NO_TITLE, R.style.AdDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAdLoadingBinding.inflate(inflater, container, false)
        startTimeout()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // Optional: set dialog width
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
    }

    private fun startTimeout() {
        timeoutHandler = Handler(Looper.getMainLooper())
        timeoutRunnable = Runnable {
            if (isAdded) {
                dismissAllowingStateLoss()
                onTimeout?.invoke()
            }
        }
        timeoutHandler?.postDelayed(timeoutRunnable!!, 15000) // 15s timeout
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timeoutHandler?.removeCallbacks(timeoutRunnable!!)
        timeoutHandler = null
        timeoutRunnable = null
        _binding = null
    }

    companion object {
        private const val TAG = "AdLoadingDialog"

        fun show(
            fragmentManager: FragmentManager,
            onTimeout: (() -> Unit)? = null
        ): AdLoadingDialogFragment {
            // Nếu đã show trước đó thì không show nữa
            val existing = fragmentManager.findFragmentByTag(TAG)
            if (existing is AdLoadingDialogFragment) return existing

            val dialog = AdLoadingDialogFragment()
            dialog.onTimeout = onTimeout
            dialog.show(fragmentManager, TAG)
            return dialog
        }

        fun dismiss(fragmentManager: FragmentManager) {
            val dialog = fragmentManager.findFragmentByTag(TAG) as? DialogFragment
            dialog?.dismissAllowingStateLoss()
        }
    }
}
