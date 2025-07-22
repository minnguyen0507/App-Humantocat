package com.pettranslator.cattranslator.catsounds.bases


import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.google.gson.Gson
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.model.AdConfig
import com.pettranslator.cattranslator.catsounds.ui.main.AdLoadingDialogFragment

abstract class BaseActivity<viewBinding : ViewBinding> :
    AppCompatActivity() {
     lateinit var viewBinding: viewBinding
    abstract fun inflateViewBinding(inflater: LayoutInflater): viewBinding
    lateinit var gson: Gson
    val handler = Handler(Looper.myLooper() ?: Looper.getMainLooper())

    fun requestPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            performPermissionTask()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        try {
            if (permissions.isNotEmpty() && grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    performPermissionTask()
                } else {
                    handlePermissionDenied(permissions[0])
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun requestUMP() {
        println("REQUEST UMP");
        val params = ConsentRequestParameters.Builder()
            .setTagForUnderAgeOfConsent(false)
            .build()

        val consentInformation = UserMessagingPlatform.getConsentInformation(this)
        consentInformation.requestConsentInfoUpdate(
            this,
            params,
            {
                println("consentInformation.isConsentFormAvailable ${consentInformation.isConsentFormAvailable}")
                if (consentInformation.isConsentFormAvailable) {
                    loadAndShowForm(consentInformation)
                }
            },
            { formError ->
                println("Consent info update failed: ${formError.message}");
            }
        )
    }

    private fun loadAndShowForm(consentInformation: ConsentInformation) {
        UserMessagingPlatform.loadConsentForm(
            this,
            { consentForm ->
                println("consentForm ${consentInformation.consentStatus == ConsentInformation.ConsentStatus.REQUIRED}")
                if (consentInformation.consentStatus == ConsentInformation.ConsentStatus.REQUIRED) {
                    consentForm.show(
                        this
                    ) {
                        // Gọi lại load để xử lý thay đổi
                        loadAndShowForm(consentInformation)
                    }
                }
            },
            { formError ->
                println("Load form error: ${formError.message}")
            }
        )
    }

    fun showAdLoadingDialog(onTimeout: (() -> Unit)? = null) {
        AdLoadingDialogFragment.show(supportFragmentManager, onTimeout)
    }

    fun dismissAdLoadingDialog() {
        AdLoadingDialogFragment.dismiss(supportFragmentManager)
    }

    private fun handlePermissionDenied(permission: String) {
        val shouldShowRationale =
            ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
        if (!shouldShowRationale) {
            showPermissionDeniedMessage(
                getString(R.string.content_record),
                getString(R.string.setting)
            ) {
                openAppSettings()
            }
        }
    }


    private fun showPermissionDeniedMessage(
        message: String,
        actionLabel: String,
        action: () -> Unit
    ) {
        val builder = AlertDialog.Builder(this)
            .setTitle(getString(R.string.rights_denied))
            .setMessage(message)
            .setPositiveButton(actionLabel) { _, _ ->
                action()
            }
            .setNegativeButton(getString(R.string.close), null)
            .setCancelable(false)

        builder.show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }


    open fun performPermissionTask() {

    }

    protected abstract fun initialize()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = inflateViewBinding(layoutInflater)
        setContentView(viewBinding.root)
        initApp()
        initialize()
        requestUMP()
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