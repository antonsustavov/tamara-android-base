package com.tamara.care.watch.presentation.setup

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.tamara.care.watch.R
import com.tamara.care.watch.data.entity.AdapterRoomEntity
import com.tamara.care.watch.data.entity.BeaconRegisterEntity
import com.tamara.care.watch.data.entity.RoomType
import com.tamara.care.watch.data.entity.getRooms
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.databinding.FragmentDialogSetupBeaconBinding
import com.tamara.care.watch.manager.SharedPreferencesManager
import com.tamara.care.watch.presentation.adapter.RoomChooseAdapter
import com.tamara.care.watch.presentation.setup.BeaconSetupFragment.Companion.EXTRA_BEACON
import com.tamara.care.watch.presentation.setup.BeaconSetupFragment.Companion.EXTRA_UPDATE_BEACON
import com.tamara.care.watch.presentation.setup.BeaconSetupFragment.Companion.ROOMS_LIST
import com.tamara.care.watch.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BeaconSetupDialogFragment : DialogFragment() {

    private val binding by lazy {
        FragmentDialogSetupBeaconBinding.inflate(layoutInflater)
    }

    private val beacon by lazy {
        arguments?.getParcelable<BeaconRegisterEntity>(EXTRA_BEACON)
    }

    private val viewModel: SetupViewModel by activityViewModels()

    private var roomName: String = ""

    @Inject
    lateinit var roomChooseAdapter: RoomChooseAdapter

    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClicks()
        setupDefaultValues()
        observeViewModel()
        setupRecyclerView()
    }

    private fun setupClicks() {
        binding.apply {
            cancelButton.setOnClickListener {
                dismiss()
            }
            saveButton.setOnClickListener {
                if (beacon?._id.isNullOrEmpty()) {
                    if (roomName.isNotEmpty()) {
                        viewModel.saveBeaconInfo(
                            BeaconRegisterEntity(
                                room = roomName,
                                macAddress = beacon?.macAddress ?: "-1",
                                transmitter = sharedPreferencesManager.transmitterId
                            )
                        )
                    } else {
                        requireContext().showToast(requireContext().getString(R.string.link_the_room))
                    }
                } else {
                    if (roomName.isNotEmpty()) {
                        viewModel.updateBeaconInfo(
                            beacon?._id!!,
                            BeaconRegisterEntity(
                                room = roomName,
                                macAddress = beacon?.macAddress ?: "-1",
                                transmitter = sharedPreferencesManager.transmitterId
                            )
                        )
                    } else if (roomName.isEmpty() && checkOccupiedRoomTypes().size == 6) {
                        dismiss()
                    } else {
                        requireContext().showToast(requireContext().getString(R.string.choose_new_room_to_update_info))
                    }
                }
            }
        }
    }

    private fun setupDefaultValues() {
        binding.apply {
            beaconMacAddressValue.text = beacon?.macAddress
            currentRoomValue.text = beacon?.room ?: "Unselected"
        }
    }

    private fun setupRecyclerView() {
        binding.roomChooseRecycler.adapter = roomChooseAdapter

        roomChooseAdapter.apply {
            submitList(generateRoomsListForAdapter())

            onRoomClickListener = {
                roomName = it.name.toString()
                binding.currentRoomValue.text = it.name
            }
        }
    }

    private fun generateRoomsListForAdapter(): ArrayList<AdapterRoomEntity> {
        val roomsList = getRooms()
        val roomsEntityList = arrayListOf<AdapterRoomEntity>()
        roomsList.forEach { roomType ->
            if (roomType in checkOccupiedRoomTypes()) {
                roomsEntityList.add(AdapterRoomEntity(roomType.key, true))
            } else {
                roomsEntityList.add(AdapterRoomEntity(roomType.key, false))
            }
        }
        return roomsEntityList
    }

    private fun checkOccupiedRoomTypes(): MutableList<RoomType> {
        val occupiedRooms = this.arguments?.getStringArrayList(ROOMS_LIST)
        val allRooms = getRooms()
        val occupiedRoomsTypes = mutableListOf<RoomType>()
        allRooms.forEach { item ->
            occupiedRooms?.forEach { roomName ->
                if (item.key == roomName) {
                    occupiedRoomsTypes.add(item)
                }
            }
        }
        return occupiedRoomsTypes
    }

    private fun observeViewModel() {
        viewModel.beaconsInfoLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ModelState.Success -> {
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(EXTRA_UPDATE_BEACON, state.data)
                    viewModel.beaconsInfoLiveData.value = null
                    dismiss()
                }
                is ModelState.Error -> {
                    requireContext().showToast(state.error.errorMessage)
                }
                is ModelState.Loading -> {

                }
            }
        }
    }
}