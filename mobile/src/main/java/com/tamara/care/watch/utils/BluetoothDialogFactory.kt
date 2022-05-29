package com.tamara.care.watch.utils

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tamara.care.watch.R
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by ArtemLampa on 28.09.2021.
 */
@Singleton
class BluetoothDialogFactory @Inject constructor() {
    fun createDialog(type: DialogType, activity: Activity): MaterialAlertDialogBuilder {
        return when (type) {
            DialogType.LOCATION_PERMISSION_ERROR -> {
                MaterialAlertDialogBuilder(activity)
                    .setTitle(activity.getString(R.string.error))
                    .setMessage(activity.getString(R.string.location_permission_error))
                    .setPositiveButton(activity.getString(R.string.open_settings)) { dialog, _ ->
                        dialog.cancel()
                        activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                    .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                        dialog.cancel()
                    }
                    .setOnDismissListener {
                        it.dismiss()
                    }
            }
            DialogType.LOCATION_SERVICES_ARE_NOT_ENABLED -> {
                MaterialAlertDialogBuilder(activity)
                    .setTitle(activity.getString(R.string.location_services_are_not_enabled))
                    .setMessage(activity.getString(R.string.scanning_for_bluetooth_peripherals_requires))
                    .setPositiveButton(activity.getString(R.string.enable)) { dialogInterface, _ ->
                        dialogInterface.cancel()
                        activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                    .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                        dialog.cancel()
                    }
            }
            DialogType.NEED_BLUETOOTH_FOR_SCANNING -> {
                MaterialAlertDialogBuilder(activity)
                    .setTitle(activity.getString(R.string.error))
                    .setMessage(activity.getString(R.string.we_need_bluetooth_for_scanning_beacons))
                    .setPositiveButton(activity.getString(R.string.open_settings)) { dialog, _ ->
                        dialog.cancel()
                        activity.startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS))
                    }
                    .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                        dialog.cancel()
                    }
                    .setOnDismissListener {
                        it?.dismiss()
                    }
            }
            else -> {
                MaterialAlertDialogBuilder(activity)
                    .setTitle(activity.getString(R.string.error))
                    .setMessage(activity.getString(R.string.no_bluetooth_adapter))
                    .setPositiveButton(activity.getString(android.R.string.cancel)) { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }
            }
        }
    }
}