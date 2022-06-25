package com.tamara.care.watch.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.impl.utils.ForceStopRunnable
import com.tamara.care.watch.speech.SpeechListener

class TrackingServiceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            val trackingIntent = Intent(context, TrackingService::class.java)
            context?.startForegroundService(trackingIntent)
            Log.i(">>>>> SERVER START", "START?????")
        }
    }
}