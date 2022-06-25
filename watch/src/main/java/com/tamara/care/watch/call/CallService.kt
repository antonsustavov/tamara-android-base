package com.tamara.care.watch.call

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LifecycleService
import javax.inject.Singleton

@Singleton
class CallService: LifecycleService() {
    companion object {
        const val TELEPHONE = "80988-987"
    }

    fun call() {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$TELEPHONE")
        startActivity(callIntent)
    }

}