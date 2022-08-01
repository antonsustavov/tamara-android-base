package com.tamara.care.watch.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.work.impl.utils.ForceStopRunnable
import com.tamara.care.watch.speech.SpeechListener

class TrackingServiceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "Receiver intent ${intent.toString()}", Toast.LENGTH_LONG).show()
        if (intent?.action.equals(Intent.ACTION_BOOT_COMPLETED) || intent?.action.equals(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE)
            || intent?.action.equals(Intent.ACTION_POWER_CONNECTED)) {
            val trackingIntent = Intent(context, TrackingService::class.java)
            context?.startForegroundService(trackingIntent)
            Log.i(">>>>> SERVER START", "START?????")
        }
    }
}