package com.tamara.care.watch.presentation

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.tamara.care.watch.R
import com.tamara.care.watch.databinding.ActivityMainBinding
import com.tamara.care.watch.utils.ManagePermissions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var managePermissions: ManagePermissions
    companion object {
        @JvmStatic
        val permissionsRequestCode = 9379995
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        managePermissions()
    }

    private fun managePermissions() {
        val permissions = listOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CALL_PRIVILEGED,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ANSWER_PHONE_CALLS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_PHONE_NUMBERS,
            Manifest.permission.READ_CALL_LOG
        )

        managePermissions = ManagePermissions(this, permissions, permissionsRequestCode)
        managePermissions.checkPermissions()
    }

}