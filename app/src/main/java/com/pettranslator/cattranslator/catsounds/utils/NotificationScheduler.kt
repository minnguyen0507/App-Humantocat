package com.pettranslator.cattranslator.catsounds.utils

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.pettranslator.cattranslator.catsounds.notification.NotificationWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

object NotificationScheduler {

    fun scheduleDailyWork(context: Context, hour: Int, minute: Int, uniqueName: String) {
        val now = Calendar.getInstance()
        val target = now.clone() as Calendar
        target.set(Calendar.HOUR_OF_DAY, hour)
        target.set(Calendar.MINUTE, minute)
        target.set(Calendar.SECOND, 0)
        if (target.before(now)) {
            target.add(Calendar.DATE, 1)
        }

        val delay = target.timeInMillis - now.timeInMillis

        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            uniqueName,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun scheduleBoth(context: Context) {
        scheduleDailyWork(context, 0, 44, "daily_8am")
        scheduleDailyWork(context, 20, 0, "daily_8pm")
    }
}
