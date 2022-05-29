package com.tamara.care.watch.data.network

import com.tamara.care.watch.manager.SharedPreferencesManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by ArtemLampa on 21.10.2021.
 */

@Singleton
class LanguageInterceptor @Inject constructor(
    private val sharedPreferencesManager: SharedPreferencesManager
):Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val userLanguage = sharedPreferencesManager.userLanguage

        val requestBuilder = chain.request().newBuilder()

        requestBuilder.addHeader("Accept-Language", userLanguage ?: "en")

        return chain.proceed(requestBuilder.build())
    }
}