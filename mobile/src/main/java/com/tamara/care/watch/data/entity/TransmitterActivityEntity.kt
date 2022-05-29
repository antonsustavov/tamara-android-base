package com.tamara.care.watch.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransmitterActivityEntity(
    val time: TimeEntity? = null,
    val status: String? = null
) : Parcelable
