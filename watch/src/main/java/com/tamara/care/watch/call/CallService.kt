package com.tamara.care.watch.call

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LifecycleService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallService @Inject constructor(): LifecycleService() {
    companion object {
        const val TELEPHONE = "0532363618"
    }

    fun call() {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$TELEPHONE")
        startActivity(callIntent)
    }

}