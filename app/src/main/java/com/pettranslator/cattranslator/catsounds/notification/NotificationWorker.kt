package com.pettranslator.cattranslator.catsounds.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.analytics.FirebaseAnalytics
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.ui.splash.SplashActivity
import com.pettranslator.cattranslator.catsounds.utils.AnalyticsHelper
import com.pettranslator.cattranslator.catsounds.utils.SharedPref
import com.pettranslator.cattranslator.catsounds.utils.getLocalizedContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.Calendar
import kotlin.random.Random

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val sharedPref: SharedPref
) : Worker(context, workerParams) {

    override fun doWork(): Result {

        val langCode = sharedPref.getLanguage()

        val localizedContext = applicationContext.getLocalizedContext(langCode.toString())

        // Tạo channel notification
        val channelId = "daily_channel"
        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val notificationType = if (hour in 5..11) "morning" else "evening"
        val analyticsHelper = AnalyticsHelper(FirebaseAnalytics.getInstance(applicationContext))
        analyticsHelper.logNotificationReceive(notificationType)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                localizedContext.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }
        val number = Random.nextInt(1, 21)
        val keyTitle = "n_title_$number"
        val keyDes = "n_des_$number"
        val intent = Intent(applicationContext, SplashActivity::class.java).apply {
            putExtra("notification_type", notificationType)
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val res = applicationContext.resources
        val packageName = applicationContext.packageName
        val titleResId = res.getIdentifier(keyTitle, "string", packageName)
        val messageResId = res.getIdentifier(keyDes, "string", packageName)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(res.getString(titleResId))
            .setContentText(res.getString(messageResId))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        manager.notify(Random.nextInt(), notification)

        return Result.success()
    }
}

