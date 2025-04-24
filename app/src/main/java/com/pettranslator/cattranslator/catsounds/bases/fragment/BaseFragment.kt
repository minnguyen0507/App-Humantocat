package com.pettranslator.cattranslator.catsounds.bases.fragment


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.pettranslator.cattranslator.catsounds.bases.BaseActivity
import com.google.gson.Gson

/**
 * Created by ThuanPx on 8/5/20.
 *
 * @viewModel -> view model
 * @viewModelClass -> class view model
 * @viewBinding -> class binding
 * @initialize -> init UI, adapter, listener...
 * @onSubscribeObserver -> subscribe observer live data
 *
 */

abstract class BaseFragment<viewBinding : ViewBinding> : Fragment() {

    private var _viewBinding: viewBinding? = null
    protected val viewBinding get() = _viewBinding!!

    abstract fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): viewBinding


    lateinit var gson: Gson


    protected abstract fun initialize()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = inflateViewBinding(inflater, container)
        _viewBinding!!.root.isClickable = isInteractable
        _viewBinding!!.root.isFocusable = isInteractable
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        progressDialog = ProgressDialog(requireContext())
        gson = Gson()
        initialize()
    }

    /**
     * Fragments outlive their views. Make sure you clean up any references to
     * the binding class instance in the fragment's onDestroyView() method.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun showLoading(isShow: Boolean) {
        (activity as? BaseActivity<*>)?.showLoading(isShow)
    }


    private var isInteractable = true
    fun setInteractable(interactable: Boolean) {
        isInteractable = interactable
    }
}
