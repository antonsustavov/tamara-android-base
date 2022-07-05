package com.tamara.care.watch.call

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LifecycleService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallService @Inject constructor() : LifecycleService() {
    companion object {
        @JvmStatic
        val TELEPHONE = "0532363618"
    }

    fun call() {
        val intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$TELEPHONE")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_FROM_BACKGROUND)
        }
        this.startActivity(intent)
    }

//    fun call(context: Context) {
//        val intent = Intent(Intent.ACTION_CALL).apply {
//            data = Uri.parse("tel:$TELEPHONE")
//            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            addFlags(Intent.FLAG_FROM_BACKGROUND)
//        }
//        context.startActivity(intent)
//    }

}