package com.tamara.care.watch.presentation

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tamara.care.watch.R
import com.tamara.care.watch.call.CallService
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.databinding.FragmentTransmitterBinding
import com.tamara.care.watch.manager.SharedPreferencesManager
import com.tamara.care.watch.speech.SpeechListener
import com.tamara.care.watch.utils.hideKeyBoard
import com.tamara.care.watch.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TransmitterFragment : Fragment() {
    lateinit var binding: FragmentTransmitterBinding
    private val viewModel: TransmitterViewModel by viewModels()
    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transmitter, container, false)
        binding.lifecycleOwner = this
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ContextCompat.checkSelfPermission(requireContext(), "android.permission.RECORD_AUDIO") != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 9379995)
        }
        if (ContextCompat.checkSelfPermission(requireContext(), "android.permission.CALL_PHONE") != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 9379996)
        }
        if (ContextCompat.checkSelfPermission(requireContext(), "android.permission.CALL_PRIVILEGED") != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CALL_PRIVILEGED), 9379997)
        }

        setupViews()
        setupViewModeCallbacks()
        startForegroundSpeechListener()
        startCallingService()
    }

    private fun startCallingService() {
        val callIntent = Intent(requireActivity(), CallService::class.java)
        requireContext().startForegroundService(callIntent)
    }

    private fun setupViews() {
        binding.buttonInfo.setOnClickListener {
            viewModel.sendTransmitterInfo()
        }
        binding.inputName.doAfterTextChanged {
            viewModel.nameLiveData.postValue(it.toString())
        }

        binding.inputName.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event?.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    (activity as MainActivity).hideKeyBoard()
                    return true
                }
                return false
            }
        })
    }

    private fun setupViewModeCallbacks() {
        viewModel.registerTransmitterLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ModelState.Success -> {
                    sharedPreferencesManager.userName = it.data.name
                    sharedPreferencesManager.transmitterId = it.data._id
                    findNavController().popBackStack()
                }
                is ModelState.Error -> {
                    requireContext().showToast(it.error.errorMessage)
                }
                is ModelState.Loading -> {

                }
            }
        }
    }

    private fun startForegroundSpeechListener() {
        if (!speechListenerServiceRunning()) {
            val speechIntent = Intent(requireActivity(), SpeechListener::class.java)
            requireContext().startForegroundService(speechIntent)
        }
    }

    private fun speechListenerServiceRunning(): Boolean {
        val systemService = requireContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningServices = systemService.getRunningServices(Integer.MAX_VALUE)
        for (runningServiceInfo in runningServices) {
            if (runningServiceInfo.service.className == SpeechListener::class.java.name) {
                return true
            }
        }

        return false
    }
}