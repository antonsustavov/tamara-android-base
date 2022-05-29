package com.tamara.care.watch.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransmitterEntity(
    val _id:String?,
    val name: String?,
    val kind: String?,
    val appType: String?,
    val activity: List<TransmitterActivityEntity>? = null,
    val languages: List<String> = arrayListOf(),
): Parcelable



