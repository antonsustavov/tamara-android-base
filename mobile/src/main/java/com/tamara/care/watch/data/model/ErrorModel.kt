package com.tamara.care.watch.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ErrorModel(val errorMessage: String) : Parcelable {

    companion object {
        const val CONNECTION_ERROR = "Connection error"
        const val SOMETHING_WENT_WRONG = "Something went wrong"
    }
}