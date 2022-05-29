package com.tamara.care.watch.utils

import javax.inject.Singleton

@Singleton
sealed class BluetoothState {
    object BluetoothEnabled : BluetoothState()
    object BluetoothDisabled : BluetoothState()
    object NoBluetoothAdapter : BluetoothState()
}
