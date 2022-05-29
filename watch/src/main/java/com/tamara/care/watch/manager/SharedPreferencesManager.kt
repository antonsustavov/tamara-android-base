package com.tamara.care.watch.manager

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@SuppressLint("CommitPrefEdits")
@Singleton
class SharedPreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val PREFERENCES_FILE_NAME = "tamara.spref"
    private val USER_NAME = "USER_NAME"
    private val TRANSMITTER_ID = "TRANSMITTER_ID"
    private val BEACONS_LIST = "BECONS_LIST"

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()


    var userName: String?
        get() = sharedPreferences.getString(USER_NAME, null)
        set(name) {
            editor.putString(USER_NAME, name)
            editor.apply()
        }

    var transmitterId: String?
        get() = sharedPreferences.getString(TRANSMITTER_ID, null)
        set(id) {
            editor.putString(TRANSMITTER_ID, id)
            editor.apply()
        }

    var beaconsInfoMap: HashMap<String, String>
        get() {
            val beaconsInfoMap = sharedPreferences.getString(BEACONS_LIST, "")
            val typeToken = object : TypeToken<HashMap<String, String>>() {}.type
            val hashMap: HashMap<String, String>? = Gson().fromJson(beaconsInfoMap, typeToken)
            return hashMap ?: HashMap()
        }
        set(value) {
            editor.putString(BEACONS_LIST, Gson().toJson(value))
            editor.apply()
        }
}