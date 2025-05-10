package com.pettranslator.cattranslator.catsounds.utils

import android.app.Activity
import android.app.UiModeManager
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.LocaleList
import android.os.Looper
import android.os.SystemClock
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLConnection
import java.util.Locale
import java.util.Timer
import java.util.TimerTask


fun Context.isInternetConnected(): Boolean {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}

@BindingAdapter("app:srcRes")
fun setImageResource(imageView: ImageView, @DrawableRes resId: Int) {
    imageView.setImageResource(resId)
}

fun Context.showToast(msg: String) {
    CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }
}

fun Context.showToastLong(msg: String) {
    CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
    }
}

private val lastToastTimes: MutableMap<String, Long> = HashMap()
fun Context.showToastOnceEvery30Seconds(msg: String) {
    val currentTime = System.currentTimeMillis()

    if (!lastToastTimes.containsKey(msg) || currentTime - lastToastTimes[msg]!! > 30000) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
        }

        lastToastTimes[msg] = currentTime
    }
}

fun Context.showToastOnceEvery15Seconds(msg: String) {
    val currentTime = System.currentTimeMillis()

    if (!lastToastTimes.containsKey(msg) || currentTime - lastToastTimes[msg]!! > 15000) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
        }

        lastToastTimes[msg] = currentTime
    }
}

fun Context.launchAnotherApplication(packageName: String) {
    val launchIntent: Intent? = packageManager.getLaunchIntentForPackage(packageName)
    if (launchIntent != null) {
        startActivity(launchIntent) //null pointer check in case package name was not found
    } else {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }
}

fun Context.isAppInstalled(packageName: String): Boolean {
    val pm = packageManager
    try {
        pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        return true
    } catch (ignored: PackageManager.NameNotFoundException) {
    }
    return false
}

fun String.getFileNameExtension(): String {
    return substring(lastIndexOf("."))
}

fun String.changeExtension(newExtension: String): String {
    return if (this.contains(".")) {
        val i = lastIndexOf('.')
        val name = substring(0, i)
        name + newExtension
    } else {
        this + newExtension
    }
}

fun Activity.hideStatusBar() {
    setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
    window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    if (Build.VERSION.SDK_INT >= 22) {
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = Color.TRANSPARENT
    }
}


fun Activity.isDarkModeEnabled(context: Context): Boolean {
    val uiModeManager = this.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
    return uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
}

fun Activity.setWindowFlag(bits: Int, on: Boolean) {
    val win = window
    val winParams = win.attributes
    if (on) {
        winParams.flags = winParams.flags or bits
    } else {
        winParams.flags = winParams.flags and bits.inv()
    }
    win.attributes = winParams
}


fun Context.rateUs() {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
    } catch (e: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }
}


fun addDelay(time: Long, onComplete: (() -> Unit)) {
    Handler(Looper.getMainLooper()).postDelayed({
        onComplete.invoke()
    }, time)
}

fun Activity.addButtonDelay(value: Long) {
    window.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
    )

    val buttonTimer = Timer()
    buttonTimer.schedule(object : TimerTask() {
        override fun run() {
            runOnUiThread {
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }
    }, value)
}

fun Activity.addDelayClick(value: Long) {
    // Tạm khóa thao tác
    window.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
    )

    // Sử dụng Coroutines để xóa bỏ việc tạm khóa sau khoảng thời gian value (miliseconds)
    CoroutineScope(Dispatchers.Main).launch {
        delay(value)
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}


fun Context.sizeFormatter(size: Long) =
    android.text.format.Formatter.formatShortFileSize(this, size)

fun <T> Context.openActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}

fun <T> Context.openActivityWithClearTask(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
}

fun <T> Context.openActivityAndClearApp(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}

fun getMonthInEng(month: Int): String {
    return when (month) {
        1 -> "Jan"
        2 -> "Feb"
        3 -> "March"
        4 -> "April"
        5 -> "May"
        6 -> "June"
        7 -> "July"
        8 -> "Aug"
        9 -> "Sep"
        10 -> "Oct"
        11 -> "Nov"
        12 -> "Dec"
        else -> "Jan"
    }
}


fun Context.getDrawableResource(restId: Int): Drawable? {
    return ResourcesCompat.getDrawable(resources, restId, theme)
}

fun Context.getColorResource(restId: Int): Int {
    return ResourcesCompat.getColor(resources, restId, theme)
}

fun startScopeFunction(onStart: (() -> Unit), onComplete: (() -> Unit)) {
    GlobalScope.launch(Dispatchers.Main) {
        onStart.invoke()
    }.invokeOnCompletion {
        GlobalScope.launch(Dispatchers.Main) {
            onComplete.invoke()
        }
    }
}

fun startMainScopeFunction(onStart: (() -> Unit), onComplete: (() -> Unit)? = null) {
    GlobalScope.launch(Dispatchers.Main) {
        onStart.invoke()
    }.invokeOnCompletion {
        onComplete?.invoke()
    }
}

fun Bitmap.resizeImage(): Bitmap {

    val width = width
    val height = height

    val scaleWidth = width / 10
    val scaleHeight = height / 10

    if (byteCount <= 100000000)
        return this

    return Bitmap.createScaledBitmap(this, scaleWidth, scaleHeight, false)
}

fun Activity.showKeyboardOnView(view: View) {
    view.post {
        view.requestFocus()
        val imgr = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imgr.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}


fun Context.getImageUri(inImage: Bitmap): Uri? {
    val bytes = ByteArrayOutputStream()
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path: String = MediaStore.Images.Media.insertImage(contentResolver, inImage, "Title", null)
    return Uri.parse(path)
}

fun Context.openUrl(url: String) {

    try {
        val uri = Uri.parse(url) // missing 'http://' will cause crashed
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(
            this, "No application can handle this request."
                    + " Please install a webbrowser", Toast.LENGTH_LONG
        ).show();
        e.printStackTrace();
    }
}



var ROTATION_FROM_ANGEL = 0f
var ROTATION_TO_ANGEL = 0f

fun View.rotateRight(): Float {
    var rotate: RotateAnimation? = null
    if (ROTATION_FROM_ANGEL == 0f && ROTATION_TO_ANGEL == 90f) {
        rotate = RotateAnimation(
            90f,
            180f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = 90f
        ROTATION_TO_ANGEL = 180f
    } else if (ROTATION_FROM_ANGEL == 90f && ROTATION_TO_ANGEL == 180f) {
        rotate = RotateAnimation(
            180f,
            270f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = 180f
        ROTATION_TO_ANGEL = 270f
    } else if (ROTATION_FROM_ANGEL == 180f && ROTATION_TO_ANGEL == 270f) {
        rotate = RotateAnimation(
            270f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = 270f
        ROTATION_TO_ANGEL = 360f
    } else if (ROTATION_FROM_ANGEL == 270f && ROTATION_TO_ANGEL == 360f) {
        rotate = RotateAnimation(
            0f,
            90f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = 0f
        ROTATION_TO_ANGEL = 90f
    } else if (ROTATION_FROM_ANGEL == 0f && ROTATION_TO_ANGEL == -90f) {
        rotate = RotateAnimation(
            -90f,
            0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = -90f
        ROTATION_TO_ANGEL = 0f
    } else if (ROTATION_FROM_ANGEL == -90f && ROTATION_TO_ANGEL == -180f) {
        rotate = RotateAnimation(
            -180f,
            -90f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = -180f
        ROTATION_TO_ANGEL = -90f
    } else if (ROTATION_FROM_ANGEL == -180f && ROTATION_TO_ANGEL == -90f) {
        rotate = RotateAnimation(
            -90f,
            0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = -90f
        ROTATION_TO_ANGEL = 0f
    } else if (ROTATION_FROM_ANGEL == -180f && ROTATION_TO_ANGEL == -270f) {
        rotate = RotateAnimation(
            -270f,
            -180f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = -270f
        ROTATION_TO_ANGEL = -180f
    } else if (ROTATION_FROM_ANGEL == 180f && ROTATION_TO_ANGEL == 90f) {
        rotate = RotateAnimation(
            90f,
            180f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = 90f
        ROTATION_TO_ANGEL = 180f
    } else if (ROTATION_FROM_ANGEL == -270f && ROTATION_TO_ANGEL == -180f) {
        rotate = RotateAnimation(
            -180f,
            -90f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = -180f
        ROTATION_TO_ANGEL = -90f
    } else if (ROTATION_FROM_ANGEL == -270f && ROTATION_TO_ANGEL == -360f) {
        rotate = RotateAnimation(
            0f,
            90f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = 0f
        ROTATION_TO_ANGEL = 90f
    } else {
        rotate = RotateAnimation(
            0f,
            90f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = 0f
        ROTATION_TO_ANGEL = 90f
    }


    ALog.d(TAG, "RIGHT___$ROTATION_FROM_ANGEL , $ROTATION_TO_ANGEL")
    // snack("$ROTATION_FROM_ANGEL , $ROTATION_TO_ANGEL")
    rotate.apply {
        fillAfter = true
        repeatCount = 0
        duration = 500
        interpolator = LinearInterpolator()
    }
    startAnimation(rotate)

    return ROTATION_TO_ANGEL
}

fun View.rotateLeft(): Float {
    var rotate: RotateAnimation? = null
    if (ROTATION_FROM_ANGEL == 0f && ROTATION_TO_ANGEL == 90f) {
        rotate = RotateAnimation(
            90f,
            0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = 90f
        ROTATION_TO_ANGEL = 0f
    } else if (ROTATION_FROM_ANGEL == 90f && ROTATION_TO_ANGEL == 180f) {
        rotate = RotateAnimation(
            180f,
            90f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = 180f
        ROTATION_TO_ANGEL = 90f
    } else if (ROTATION_FROM_ANGEL == 180f && ROTATION_TO_ANGEL == 270f) {
        rotate = RotateAnimation(
            270f,
            180f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = 270f
        ROTATION_TO_ANGEL = 180f
    } else if (ROTATION_FROM_ANGEL == 270f && ROTATION_TO_ANGEL == 180f) {
        rotate = RotateAnimation(
            180f,
            90f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = 180f
        ROTATION_TO_ANGEL = 90f
    } else if (ROTATION_FROM_ANGEL == 180f && ROTATION_TO_ANGEL == 90f) {
        rotate = RotateAnimation(
            90f,
            0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = 90f
        ROTATION_TO_ANGEL = 0f
    } else if (ROTATION_FROM_ANGEL == 270f && ROTATION_TO_ANGEL == 360f) {
        rotate = RotateAnimation(
            360f,
            270f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = 360f
        ROTATION_TO_ANGEL = 270f
    } else if (ROTATION_FROM_ANGEL == 360f && ROTATION_TO_ANGEL == 270f) {
        rotate = RotateAnimation(
            270f,
            180f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = 270f
        ROTATION_TO_ANGEL = 180f
    } else if (ROTATION_FROM_ANGEL == 0f && ROTATION_TO_ANGEL == -90f) {
        rotate = RotateAnimation(
            -90f,
            -180f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = -90f
        ROTATION_TO_ANGEL = -180f
    } else if (ROTATION_FROM_ANGEL == -90f && ROTATION_TO_ANGEL == -180f) {
        rotate = RotateAnimation(
            -180f,
            -270f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = -180f
        ROTATION_TO_ANGEL = -270f
    } else if (ROTATION_FROM_ANGEL == -180f && ROTATION_TO_ANGEL == -270f) {
        rotate = RotateAnimation(
            -270f,
            -360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = -270f
        ROTATION_TO_ANGEL = -360f
    } else if (ROTATION_FROM_ANGEL == -270f && ROTATION_TO_ANGEL == -360f) {
        rotate = RotateAnimation(
            0f,
            -90f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = 0f
        ROTATION_TO_ANGEL = -90f
    } else if (ROTATION_FROM_ANGEL == -270f && ROTATION_TO_ANGEL == -180f) {
        rotate = RotateAnimation(
            -180f,
            -90f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = -180f
        ROTATION_TO_ANGEL = -90f
    } else if (ROTATION_FROM_ANGEL == -180f && ROTATION_TO_ANGEL == -90f) {
        rotate = RotateAnimation(
            -90f,
            -180f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = -90f
        ROTATION_TO_ANGEL = -180f
    } else {
        rotate = RotateAnimation(
            0f,
            -90f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        ROTATION_FROM_ANGEL = 0f
        ROTATION_TO_ANGEL = -90f
    }


    ALog.d(TAG, "LEFT___$ROTATION_FROM_ANGEL , $ROTATION_TO_ANGEL")
    //  snack("$ROTATION_FROM_ANGEL , $ROTATION_TO_ANGEL")

    rotate.apply {
        fillAfter = true
        repeatCount = 0
        duration = 500
        interpolator = LinearInterpolator()
    }
    startAnimation(rotate)

    return ROTATION_TO_ANGEL
}

private const val TAG = "ExtensionFunctions"

fun Context.getLocalizedContext(languageCode: String): Context {
    val locale = Locale.forLanguageTag(languageCode)
    val localeList = LocaleList(locale)
    LocaleList.setDefault(localeList)

    val config = resources.configuration
    config.setLocales(localeList)

    return createConfigurationContext(config)
}

fun ImageView.getBitmapFromImageView(): Bitmap {
    invalidate()
    val drawable: BitmapDrawable = drawable as BitmapDrawable
    return drawable.bitmap
}

fun loadBitmap(url: Uri?): Bitmap? {
    var bm: Bitmap? = null
    var `is`: InputStream? = null
    var bis: BufferedInputStream? = null
    try {
        val conn: URLConnection = URL(url.toString()).openConnection()
        conn.connect()
        `is` = conn.getInputStream()
        bis = BufferedInputStream(`is`, 8192)
        bm = BitmapFactory.decodeStream(bis)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        if (bis != null) {
            try {
                bis.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        if (`is` != null) {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    return bm
}

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Activity.copyToClipboard(text: String) {
    val clipboardManager =
        getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    clipboardManager.text = text
    Toast.makeText(applicationContext, "Copied", Toast.LENGTH_SHORT).show()
}

fun Context.share(text: String, subject: String = ""): Boolean {
    try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(Intent.createChooser(intent, null))
        return true
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
        return false
    }
}


fun Activity.changeStatusBarColor(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val window: Window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, color)
    }
}

@RequiresApi(Build.VERSION_CODES.M)
fun Activity.changeStatusBarWindow(color: Int) {
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    window.statusBarColor = ContextCompat.getColor(this, color)
}

fun Context.saveBitmapToGallery(bitmap: Bitmap, title: String) {
    val values = ContentValues()
    values.put(MediaStore.Images.Media.TITLE, title)
    values.put(MediaStore.Images.Media.DISPLAY_NAME, title)
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
    values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
    values.put(MediaStore.Images.Media.DATE_MODIFIED, System.currentTimeMillis() / 1000)
    values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    values.put(MediaStore.Images.Media.IS_PENDING, 1)

    val uri = this.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

    uri?.let { imageUri ->
        try {
            val outputStream = this.contentResolver.openOutputStream(imageUri)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream!!)
            outputStream?.close()
            values.clear()
            values.put(MediaStore.Images.Media.IS_PENDING, 0)
            this.contentResolver.update(imageUri, values, null, null)
            // Tạo thông báo hoặc thông báo thành công sau khi lưu ảnh
        } catch (e: Exception) {
            // Xử lý lỗi khi lưu ảnh
        }
    }
}

fun Context.getBitmapFromAsset(strName: String): Bitmap? {
    val assetManager = this.assets
    return try {
        val istr = assetManager.open(strName)
        BitmapFactory.decodeStream(istr)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

fun resizeBitmapToMaxWidth(bitmap: Bitmap, maxWidth: Int): Bitmap {
    val width = bitmap.width
    val height = bitmap.height

    if (width <= maxWidth) {
        // Không cần resize nếu chiều rộng nhỏ hơn hoặc bằng maxWidth
        return bitmap
    }

    // Tính toán tỉ lệ giảm kích thước
    val scaleRatio = maxWidth.toFloat() / width.toFloat()

    // Tạo matrix để thực hiện scaling
    val matrix = Matrix()
    matrix.postScale(scaleRatio, scaleRatio)

    // Tạo bitmap mới với kích thước đã được giảm
    return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
}

fun Context.createAppFolderInPictures(appName: String): File? {
    val contentResolver = applicationContext.contentResolver

    // Kiểm tra xem thư mục đã tồn tại chưa
    val existingFolderUri =
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            .buildUpon()
            .appendQueryParameter(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + File.separator + appName
            )
            .build()

    val cursor = contentResolver.query(existingFolderUri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndex(MediaStore.MediaColumns.DATA)
            val folderPath = it.getString(columnIndex)
            return File(folderPath)
        }
    }

    // Nếu thư mục chưa tồn tại, tạo mới
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, appName)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
        put(
            MediaStore.MediaColumns.RELATIVE_PATH,
            Environment.DIRECTORY_PICTURES + File.separator + appName
        )
    }

    val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    uri?.let {
        val file = File(uri.path ?: "")
        if (file.exists()) {
            return file
        }
    }

    return null
}

fun Context.openPlayStoreUpdateApp(context: Context) {
    val appPackageName = context.packageName
    try {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$appPackageName")
            )
        )
    } catch (e: ActivityNotFoundException) {
        // Nếu không cài đặt Google Play Store, mở trình duyệt với liên kết web
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
            )
        )
    }
}

fun Context.getBitmapFromUrl(context: Context, imageUrl: String, callback: (Bitmap?) -> Unit) {
    Glide.with(context)
        .asBitmap()
        .load(imageUrl)
        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
        .into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                callback(resource)
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
                callback(null)
            }
        })
}

fun View.setSafeOnClickListener(interval: Long = 2000, onSafeClick: (View) -> Unit) {
    var lastClickTime: Long = 0

    setOnClickListener { v ->
        val clickTime = SystemClock.elapsedRealtime()

        if (clickTime - lastClickTime < interval) {
            // Người dùng nhấn quá nhanh, tránh double click
            return@setOnClickListener
        }

        // Thực hiện công việc của bạn ở đây
        onSafeClick(v)
        lastClickTime = clickTime
    }
}