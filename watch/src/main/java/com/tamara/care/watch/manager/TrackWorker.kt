package com.tamara.care.watch.manager

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tamara.care.watch.service.TrackingService

class TrackWorker(private val appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    val LOG_TAG = "TrackWorker"
    override fun doWork(): Result {
        return try {
            //if (!isServiceRunning()) {
            appContext.startForegroundService(Intent(appContext, TrackingService::class.java))
            //}
            Log.d(LOG_TAG, "TrackWorker success")
            Result.success()
        } catch (e: Exception) {
            Log.d(LOG_TAG, "TrackWorker failure")
            Result.failure()
        }


    }

//    private fun isServiceRunning(): Boolean {
//        val manager = appContext.getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
//        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
//            if ("com.tamara.care.watch.service.TrackingService" == service.service.className) {
//                return true
//            }
//        }
//        return false
//    }
}