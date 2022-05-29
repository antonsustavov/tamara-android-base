package com.tamara.care.watch.utils

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.tamara.care.watch.TamaraCareApplication
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothUtils @Inject constructor() {
    fun bluetoothState(): BluetoothState {
        return if (hasBluetoothAdapter()) {
            if (isBluetoothEnabled()) {
                BluetoothState.BluetoothEnabled
            } else {
                BluetoothState.BluetoothDisabled
            }
        } else {
            BluetoothState.NoBluetoothAdapter
        }
    }

    fun hasBluetoothAdapter(): Boolean {
        return bluetoothAdapter() != null
    }

    fun isBluetoothEnabled(): Boolean {
        val adapter = bluetoothAdapter() ?: return false
        return adapter.isEnabled
    }

    private fun bluetoothAdapter(): BluetoothAdapter? {
        return try {
            val bluetoothManager = TamaraCareApplication.getAppContext().getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
            bluetoothManager?.adapter
        } catch (error: Throwable) {
            null
        }
    }
}
