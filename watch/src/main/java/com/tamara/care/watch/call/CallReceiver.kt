package com.tamara.care.watch.call

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.tamara.care.watch.utils.ManagePermissions

class CallReceiver : BroadcastReceiver() {
    private lateinit var managePermissions: ManagePermissions

    companion object {
        @JvmStatic
        val TELEPHONE = "0532363618"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val state = intent!!.getStringExtra(TelephonyManager.EXTRA_STATE)
        Log.d("STATE", state.toString())
        val telephonyAnswer = context!!.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        val callingNumber = intent!!.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ANSWER_PHONE_CALLS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            managePermissions.checkPermissions()
        }
        Log.d("NUMBER", "incoming phone number: $callingNumber")
        if (TELEPHONE == callingNumber) {
            telephonyAnswer.acceptRingingCall()
        }
    }
}