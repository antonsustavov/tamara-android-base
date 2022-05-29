package com.tamara.care.watch.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PermissionsUtils @Inject constructor(
    @ApplicationContext val context: Context
) {

    fun missingRequiredBluetoothPermissions(): Array<String> {
        return missingBluetoothPermissions(requiredBluetoothPermissions())
    }

    private fun missingBluetoothPermissions(requiredPermissions: Array<String>): Array<String> {
        val missingPermissions: MutableList<String> = mutableListOf()
        for (requiredPermission in requiredPermissions) {
            if (context.applicationContext.checkSelfPermission(requiredPermission) != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(requiredPermission)
            }
        }
        return missingPermissions.toTypedArray()
    }

    private fun requiredBluetoothPermissions(): Array<String> {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            else -> arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }
}
