package com.tamara.care.watch.presentation.setup

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.tamara.care.watch.R
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.databinding.FragmentDialogSetupTransmitterBinding
import com.tamara.care.watch.manager.SharedPreferencesManager
import com.tamara.care.watch.presentation.setup.BeaconSetupFragment.Companion.EXTRA_TRANSMITTER
import com.tamara.care.watch.presentation.setup.BeaconSetupFragment.Companion.EXTRA_UPDATE_TRANSMITTER
import com.tamara.care.watch.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TransmitterSetupDialogFragment : DialogFragment() {

    lateinit var binding: FragmentDialogSetupTransmitterBinding

    private val transmitterId by lazy {
        arguments?.getString(EXTRA_TRANSMITTER)
    }

    private val viewModel: SetupViewModel by activityViewModels()

    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dialog_setup_transmitter,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setupClicks()
    }

    private fun observeViewModel() {
        viewModel.transmitterInfoLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ModelState.Success -> {
                    if (state.data.items.isNotEmpty()) {
                        sharedPreferencesManager.transmitterId = state.data.items.first()._id
                        findNavController().previousBackStackEntry?.savedStateHandle?.set(
                            EXTRA_UPDATE_TRANSMITTER,
                            state.data.items.first()
                        )
                        viewModel.transmitterInfoLiveData.value = null
                        dismiss()
                    } else {
                        requireContext().showToast(getString(R.string.transmitter_does_not_exist))
                    }
                }
                is ModelState.Error -> {
                    requireContext().showToast("invalid transmitter id")
                    viewModel.transmitterInfoLiveData.value = null
                }
                is ModelState.Loading -> {

                }
            }
        }
    }

    private fun setupClicks() {
        binding.apply {
            transmitterId?.let {
                this.transmitterIdEditText.setText(it)
            }
            save.setOnClickListener {
                if (!binding.transmitterIdEditText.text.isNullOrEmpty()) {
                    viewModel.getTransmitter(
                        binding.transmitterIdEditText.text.toString().trim()
                    )
                }
            }
        }
    }
}