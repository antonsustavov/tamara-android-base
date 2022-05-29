package com.tamara.care.watch.presentation.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.tamara.care.watch.data.entity.EmptyTimeEntity
import com.tamara.care.watch.data.entity.TimeEntity
import com.tamara.care.watch.utils.getDateFromServerTime
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("app:time")
fun TextView?.time(time: String) {
    val currentTime = time.getDateFromServerTime()
    this?.text = SimpleDateFormat("EEE, MMM d, HH:mm", Locale.getDefault()).format(currentTime!!)
}

@SuppressLint("SetTextI18n")
@BindingAdapter("timeWithSeconds")
fun TextView?.timeWithSeconds(value: EmptyTimeEntity) {
    val from = value.from.getDateFromServerTime()
    val to = value.to.getDateFromServerTime()
    var startTime = ""
    var endTime = ""
    if (from != null) {
        startTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(from)
    }
    if (to != null) {
        endTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(to)
    }
    this?.text = "$startTime - $endTime"
}

@SuppressLint("SetTextI18n")
@BindingAdapter("timeWithSeconds")
fun TextView?.timeWithSeconds(value: TimeEntity) {
    val from = value.from.getDateFromServerTime()
    val to = value.to.getDateFromServerTime()
    var startTime = ""
    var endTime = ""
    if (from != null) {
        startTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(from)
    }
    if (to != null) {
        endTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(to)
    }
    this?.text = "$startTime - $endTime"
}

@SuppressLint("SetTextI18n")
@BindingAdapter("fullDateWithSeconds")
fun TextView?.fullDateWithSeconds(value: TimeEntity) {
    val from = value.from.getDateFromServerTime()
    val to = value.to.getDateFromServerTime()
    var startTime = ""
    var endTime = ""
    if (from != null) {
        startTime = SimpleDateFormat("yy.MM.dd - HH:mm", Locale.getDefault()).format(from)
    }
    if (to != null) {
        endTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(to)
    }
    this?.text = "$startTime-$endTime"
}

@SuppressLint("SetTextI18n")
@BindingAdapter("proximityValue", "rssiValue")
fun TextView.setProximityValue(roomName: String?, rssiValue: Int?) {
    if (!roomName.isNullOrEmpty() && rssiValue != null) {
        text = "$roomName ($rssiValue)"
    } else {
        visibility = View.GONE
    }
}

