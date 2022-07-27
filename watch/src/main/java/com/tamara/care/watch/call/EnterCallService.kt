package com.tamara.care.watch.call

import android.telecom.Call
import android.telecom.InCallService
import android.telecom.VideoProfile

class EnterCallService: InCallService() {

    override fun onCallAdded(call: Call?) {
        super.onCallAdded(call)
        call?.answer(VideoProfile.STATE_AUDIO_ONLY)
    }
}