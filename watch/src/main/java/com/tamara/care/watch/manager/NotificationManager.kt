package com.tamara.care.watch.manager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Context
import androidx.core.app.NotificationCompat
import com.tamara.care.watch.R

class NotificationManager(
    private val context: Context
) {
    companion object {
        const val CHANNEL_ID = "tamara_notification_channel"
        const val CHANNEL_NAME = "tamara_notification"
    }

    private var notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private var notification: Notification? = null

    init {
        createNotificationChannel()
    }

    fun createNotification(title: String, description: String) {
        notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(description)
            //.setContentIntent(pendingIntent)
            //.setColor(ContextCompat.getColor(context, R.color.black))
            .setSmallIcon(R.mipmap.ic_launcher)
            //.setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }

    fun getCurrentNotification(): Notification {
        notification?.let { return it }
        return Notification()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
    }

}