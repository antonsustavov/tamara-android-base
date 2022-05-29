package com.tamara.care.watch.manager

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@SuppressLint("CommitPrefEdits")
@Singleton
class SharedPreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val PREFERENCES_FILE_NAME = "tamara.spref"
    private val USER_NAME = "USER_NAME"
    private val TRANSMITTER_ID = "TRANSMITTER_ID"
    private val USER_LANGUAGE = "USER_LANGUAGE"
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()


//    var userName: String?
//        get() = sharedPreferences.getString(USER_NAME, null)
//        set(name) {
//            editor.putString(USER_NAME, name)
//            editor.apply()
//        }

    var transmitterId: String?
        get() = sharedPreferences.getString(TRANSMITTER_ID, null)
        set(id) {
            editor.putString(TRANSMITTER_ID, id)
            editor.apply()
        }

    var userLanguage: String?
        get() = sharedPreferences.getString(USER_LANGUAGE, "en")
        set(language) {
            editor.putString(USER_LANGUAGE, language)
            editor.apply()
        }

}