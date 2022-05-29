package com.tamara.care.watch.manager

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectionManager @Inject constructor(@ApplicationContext context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager?

    var isWiFiConnected = MutableLiveData(isAvailableWiFiConnection())  // with initial value


    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager?.registerDefaultNetworkCallback(object :
                ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    isWiFiConnected.postValue(true)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    isWiFiConnected.postValue(false)
                }

            })
        }
    }


    @Suppress("DEPRECATION")
    private fun isAvailableWiFiConnection(): Boolean {
        val network = connectivityManager?.activeNetwork
        val capabilities = connectivityManager?.getNetworkCapabilities(network)
        return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
                || capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
    }

}