package com.tamara.care.watch.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BeaconRegisterEntity(
    var _id: String? = null,
    var room: String,
    var macAddress: String,
    var transmitter: String?
) : Parcelable