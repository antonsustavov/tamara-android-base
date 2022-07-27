package com.tamara.care.watch.call

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.telecom.TelecomManager
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat

class CallReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val telephonyAnswer = context!!.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        telephonyAnswer.acceptRingingCall()

        val telephony = context!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val number = intent!!.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
        Log.d("NUMBER", ">>>>>>>>>>>>> phone $number")
        telephony.listen(object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, incomingNumber: String) {
                super.onCallStateChanged(state, incomingNumber)
                val msg = "incomingNumber : $incomingNumber"
                val toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
                toast.show()
            }
        }, PhoneStateListener.LISTEN_CALL_STATE)
    }
}