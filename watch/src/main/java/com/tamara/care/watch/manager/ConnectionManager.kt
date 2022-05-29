package com.tamara.care.watch.manager

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectionManager @Inject constructor(@ApplicationContext context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager?
    private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    var isWiFiConnected = MutableLiveData(isAvailableWiFiConnection())  // with initial value
    //var isBluetoothConnected = MutableLiveData(isAvailableBluetoothConnection())  // with initial value


    init {
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

    fun isAvailableBluetoothConnection(): Boolean {
        return bluetoothAdapter.isEnabled
    }

    fun enableBluetooth() {
        bluetoothAdapter.enable()
    }


    @Suppress("DEPRECATION")
    private fun isAvailableWiFiConnection(): Boolean {
        val network = connectivityManager?.activeNetwork
        val capabilities = connectivityManager?.getNetworkCapabilities(network)
        return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
                || capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
    }

}