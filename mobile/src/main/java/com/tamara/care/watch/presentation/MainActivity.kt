package com.tamara.care.watch.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import com.tamara.care.watch.R
import com.tamara.care.watch.data.entity.TransmitterStatus
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.databinding.ActivityMainBinding
import com.tamara.care.watch.manager.SharedPreferencesManager
import com.tamara.care.watch.presentation.chat.ChatFragment
import com.tamara.care.watch.presentation.patient.PatientFragment
import com.tamara.care.watch.presentation.proximity.ProximityFragment
import com.tamara.care.watch.presentation.setup.BeaconSetupFragment
import com.tamara.care.watch.presentation.therapist.TherapistFragment
import com.tamara.care.watch.utils.showToast
import com.tamara.care.watch.utils.slide
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainActivityListener {
    companion object {
        const val ENGLISH_LANGUAGE = "en"
        const val HEBREW_LANGUAGE = "iw"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = findNavController(R.id.nav_host_fragment_content_main)
        if (sharedPreferencesManager.transmitterId.isNullOrEmpty()) {
            navController.navigate(R.id.toSetupFragment)
        }
        initDrawer()
        setupClicks()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        getDeviceLanguage()
    }

    private fun getDeviceLanguage() {
        val language = Locale.getDefault()
        if (language.toString().slice(0..1) != HEBREW_LANGUAGE) {
            sharedPreferencesManager.userLanguage = ENGLISH_LANGUAGE
        } else {
            sharedPreferencesManager.userLanguage = Locale.getDefault().toString().slice(0..1)
        }
    }

    private fun setupClicks() {
        binding.apply {
            messageIcon.setOnClickListener {
                val fragment = getCurrentFragment()
                if (fragment !is ChatFragment) {
                    navController.navigate(R.id.toChatFragment)
                }
            }
            call.setOnClickListener {
                makeCall()
            }
            therapistIcon.setOnClickListener {
                val fragment = getCurrentFragment()
                if (fragment !is TherapistFragment) {
                    navController.navigate(R.id.toTherapistFragment)
                }
            }
        }
    }

    private fun getCurrentFragment(): Fragment {
        val navHostFragment = supportFragmentManager.primaryNavigationFragment
        return navHostFragment!!.childFragmentManager.fragments[0]
    }

    private fun observeViewModel() {
        viewModel.parametersLiveData.observe(this) { state ->
            when (state) {
                is ModelState.Success -> {
                    if (state.data.items.isNotEmpty()) {
                        if (state.data.items.first().activity?.isNotEmpty() == true) {
                            showIndicatorImage(state.data.items.first().activity?.first()?.status)
                        } else {
                            showIndicatorImage()
                        }
                    }
                }
                is ModelState.Error -> {
                    showToast(state.error.errorMessage)
                }
                is ModelState.Loading -> {

                }
            }
        }
    }

    fun showIndicatorImage(status: String? = null) {
        if (status == TransmitterStatus.ACTIVE.key) {
            binding.therapistConnectedIcon.isVisible = true
            binding.therapistDisconnectedIcon.isVisible = false

        } else {
            binding.therapistConnectedIcon.isVisible = false
            binding.therapistDisconnectedIcon.isVisible = true
        }
    }

    private fun initDrawer() {
        // binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        binding.navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
            when {
                item.itemId == R.id.proximity && getCurrentFragment() !is ProximityFragment -> {
                    navController.navigate(R.id.toProximityFragment)
                }
                item.itemId == R.id.therapist_details && getCurrentFragment() !is TherapistFragment -> {
                    navController.navigate(R.id.toTherapistFragment)
                }
                item.itemId == R.id.way_of_treatment -> {
                    showToast("in development")
                    //navController.navigate(R.id.BeaconSetupFragment)
                }
                item.itemId == R.id.financial -> {
                    showToast("in development")
                    //navController.navigate(R.id.BeaconSetupFragment)
                }
                item.itemId == R.id.health_indices && getCurrentFragment() !is PatientFragment -> {
                    navController.navigate(R.id.toPatientFragment)
                }
            }
            binding.drawer.closeDrawer(GravityCompat.END)
            return@OnNavigationItemSelectedListener false
        })
        binding.close.setOnClickListener {
            binding.drawer.closeDrawer(GravityCompat.END)
        }
    }

    override fun openDrawer() {
        binding.drawer.openDrawer(GravityCompat.END)
    }

    private fun makeCall() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + "999999999")
        startActivity(intent)
    }

    override fun hideBottomNavBar() {
        binding.bottom.slide(false)
//        binding.bottom.visibility = View.GONE
    }

    override fun showBottomNavBar() {
        binding.bottom.visibility = View.VISIBLE
    }

    override fun getTransmitterInfo() {
        viewModel.getParametersInfo()
    }

    override fun onBackPressed() {
        if (getCurrentFragment() is BeaconSetupFragment) {
            if (sharedPreferencesManager.transmitterId.isNullOrEmpty()) {
                (getCurrentFragment() as BeaconSetupFragment).openTransmitterDialog()
            } else {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }
}

interface MainActivityListener {
    fun openDrawer()
    fun hideBottomNavBar()
    fun showBottomNavBar()
    fun getTransmitterInfo()
}