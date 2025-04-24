package com.pettranslator.cattranslator.catsounds.bases


import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.google.gson.Gson

abstract class BaseActivity<viewBinding : ViewBinding> :
    AppCompatActivity() {
    protected lateinit var viewBinding: viewBinding
    abstract fun inflateViewBinding(inflater: LayoutInflater): viewBinding
    lateinit var gson: Gson
    lateinit var languageLauncher: ActivityResultLauncher<Intent>
    val handler = Handler(Looper.myLooper() ?: Looper.getMainLooper())


    protected abstract fun initialize()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = inflateViewBinding(layoutInflater)
        setContentView(viewBinding.root)
        initApp()
        initialize()
    }

    private fun initApp() {
        gson = Gson()

        onSetPremium()
    }

    private fun onSetPremium() {
//        utilsViewModel.setPremiumUser(sharedPeref.getBoolean(SharedConstants.isPremium, false))
    }

    fun showLoading(isShow: Boolean) {
//        if (isShow && progressDialog?.isShowing.isNotTrue() && !isFinishing) {
//            progressDialog?.show()
//            handler.postDelayed({
//                if (progressDialog?.isShowing.isTrue() && !isFinishing) {
//                    try {
//                        progressDialog?.dismiss()
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                }
//            }, 20000)
//        } else if (progressDialog?.isShowing.isTrue() && !isFinishing) {
//            try {
//                progressDialog?.dismiss()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
    }

    fun showLoadingRequestNetwork(isShow: Boolean) {
//        if (isShow && progressDialog?.isShowing.isNotTrue() && !isFinishing) {
//            progressDialog?.showNewDialog()
//            handler.postDelayed({
//                if (progressDialog?.isShowing.isTrue() && !isFinishing) {
//                    try {
//                        progressDialog?.hideDialog()
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                }
//            }, 120000)
//        } else if (progressDialog?.isShowing.isTrue() && !isFinishing) {
//            try {
//                progressDialog?.hideDialog()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
    }



    fun isAppRunningInBackground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcesses = activityManager.runningAppProcesses

        for (processInfo in runningAppProcesses) {
            if (processInfo.processName == packageName) {
                // Kiểm tra xem ứng dụng có trạng thái foreground không
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return false
                }
            }
        }

        return true
    }

}