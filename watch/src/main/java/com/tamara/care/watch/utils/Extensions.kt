package com.tamara.care.watch.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


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
    val format = SimpleDateFormat("dd MMMM", Locale.US)
    return format.format(date)
}
