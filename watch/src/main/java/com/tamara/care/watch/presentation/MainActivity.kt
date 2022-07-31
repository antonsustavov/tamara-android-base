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
//    private val permissionsRequestCode = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        //requestAccessNotificationPolicyPermission()
//        checkPermissions()
        managePermissions()
    }

//    private fun checkPermissions() {
//        if (ContextCompat.checkSelfPermission(this, "android.permission.RECORD_AUDIO") != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 9379995)
//        }
//        if (ContextCompat.checkSelfPermission(this, "android.permission.CALL_PHONE") != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 9379996)
//        }
//        if (ContextCompat.checkSelfPermission(this, "android.permission.CALL_PRIVILEGED") != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(arrayOf(Manifest.permission.CALL_PRIVILEGED), 9379997)
//        }
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 9379998)
//        }
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 9379999)
//        }
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(arrayOf(Manifest.permission.ANSWER_PHONE_CALLS), 9380000)
//        }
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE), 9380001)
//        }
//    }

    private fun managePermissions() {
        val permissions = listOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CALL_PRIVILEGED,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ANSWER_PHONE_CALLS,
            Manifest.permission.READ_PHONE_STATE,
        )

        managePermissions = ManagePermissions(this, permissions, permissionsRequestCode)
        managePermissions.checkPermissions()
    }

//    private fun requestAccessNotificationPolicyPermission() {
//        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        if (!notificationManager.isNotificationPolicyAccessGranted()) {
//            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
//            this.startActivity(intent)
//        }
//    }
}