package com.tamara.care.watch.speech

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class SpeechListenerBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            val speechIntent = Intent(context, SpeechListener::class.java)
            context?.startForegroundService(speechIntent)
        }
    }
}