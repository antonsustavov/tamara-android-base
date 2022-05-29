package com.tamara.care.watch.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.transition.Slide
import androidx.transition.TransitionManager
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

const val PATTERN_SERVER = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"
fun Activity.hideKeyBoard(): Unit {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }
}

fun Activity.showKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun Activity.getScreenWidth() = this.resources.displayMetrics.widthPixels

fun Activity.getScreenHeight() = this.resources.displayMetrics.heightPixels

fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, "" + text, duration).show()
}

fun CharSequence?.isValidEmail() =
    !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String?.getErrorMessage(): String? {
    var message: String? = null
    try {
        val json = JSONObject(this)
        if (json.has("error")) {
            message = json.getString("error")
        }
    } catch (e: Exception) {
    }
    return message
}


fun Bitmap.convertBitmapToFile(file: File) {
    file.createNewFile()
    val bos = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 100, bos)
    FileOutputStream(file).use {
        it.write(bos.toByteArray())
        it.flush()
    }
}

fun Long.toDateString(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd MMMM", Locale.getDefault())
    return format.format(date)
}

@SuppressLint("SimpleDateFormat")
fun String?.getDateFromServerTime(): Date? {
    var date: Date? = null
    if (this == null)
        return null
    try {
        val formatter = SimpleDateFormat(PATTERN_SERVER, Locale.ENGLISH)
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        date = formatter.parse(this)
    } catch (e: Exception) {}
    return date
}

fun View.slide(isShow: Boolean) {
    val transition =  Slide(Gravity.BOTTOM)
    transition.duration = 50
    transition.addTarget(this)

    TransitionManager.beginDelayedTransition(this.parent as ViewGroup, transition)
    this.visibility = if (isShow) View.VISIBLE else View.GONE
}

fun <T> MutableLiveData<T>.forceRefresh() {
    this.value = this.value
}

fun String?.toCurrentTimeZone(@DateFormat pattern: String): String {
    val utcFormatter = SimpleDateFormat(pattern, Locale.getDefault())
    utcFormatter.timeZone = TimeZone.getTimeZone("GMT+0")
    val utcText = utcFormatter.parse(this)

    val defaultFormatter = SimpleDateFormat(pattern, Locale.getDefault())
    defaultFormatter.timeZone = TimeZone.getDefault()

    return defaultFormatter.format(utcText)
}

